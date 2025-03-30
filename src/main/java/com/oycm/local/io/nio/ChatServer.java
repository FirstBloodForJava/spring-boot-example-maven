package com.oycm.local.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ChatServer {

    /**
     * key为客户端名称，value为客户端和服务端之间的通道
     */
    private static Map<String, SocketChannel> clientsMap = new HashMap();

    public static void main(String[] args) throws IOException {
        int[] ports = new int[]{8081, 8082, 8083};
        // 创建多路复用器
        Selector selector = Selector.open();

        for (int port : ports) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);

            ServerSocket serverSocket = serverSocketChannel.socket();

            // 端口绑定
            serverSocket.bind(new InetSocketAddress(port));
            System.out.println("服务端启动成功，端口" + port);

            // 服务端选择器注册通道，并标识该通道所感兴趣的事件是：接收客户端连接(接收就绪)
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        }

        while (true) {
            // 一直阻塞，直到选择器上存在已经就绪的通道（包含感兴趣的事件）
            selector.select();
            // selectionKeys包含了所有通道与选择器之间的关系（接收连接、读、写）
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            // 如果 selector 中有多个就绪通道（接收就绪、读就绪、写就绪等），则遍历这些通道
            while (keyIterator.hasNext()) {
                SelectionKey selectedKey = keyIterator.next();
                String receive = null;
                // 与客户端交互的通道
                SocketChannel clientChannel;
                try {
                    // 接收就绪（已经可以接收客户端的连接了）
                    if (selectedKey.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) selectedKey.channel();
                        clientChannel = server.accept();
                        // 切换到非阻塞模式
                        clientChannel.configureBlocking(false);

                        // 再在服务端的选择器上，注册第二个通道，并标识该通道所感兴趣的事件是：接收客户端发来的消息（读就绪）
                        clientChannel.register(selector, SelectionKey.OP_READ);

                        // UUID作为 客户端唯一key
                        String key = UUID.randomUUID().toString();
                        // 将该建立完毕连接的 channel 缓存
                        clientsMap.put(key, clientChannel);

                    } else if (selectedKey.isReadable()) {
                        //读就绪（已经可以读取客户端发来的信息了）
                        clientChannel = (SocketChannel) selectedKey.channel();
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        int result = -1;
                        try {
                            // 将服务端读取到的客户端消息，放入 readBuffer 中
                            result = clientChannel.read(readBuffer);
                            // 如果终止客户端，则read()会抛出IOException异常，根据这个判断客户端退出
                        } catch (IOException e) {
                            // 获取退出连接的client对应的key
                            String clientKey = getClientKey(clientChannel);
                            System.out.println("客户端" + clientKey + "退出聊天室");
                            clientsMap.remove(clientKey);
                            clientChannel.close();
                            selectedKey.cancel();

                            continue;
                        }
                        if (result > 0) {
                            // 读取数据
                            readBuffer.flip();
                            Charset charset = StandardCharsets.UTF_8;
                            receive = String.valueOf(charset.decode(readBuffer).array());

                            System.out.println(clientChannel + ": " + receive);
                            // 处理客户端第一次发来的连接测试信息
                            if ("connecting".equals(receive)) {
                                receive = "客户端加入聊天!";
                            }
                            // 将读取到的客户消息保存在 attachment 中，用于后续向所有客户端转发此消息
                            selectedKey.attach(receive);
                            // 将通道所感兴趣的事件标识为：向客户端发送消息（写就绪）
                            selectedKey.interestOps(SelectionKey.OP_WRITE);
                        }

                    } else if (selectedKey.isWritable()) {
                        //写就绪
                        clientChannel = (SocketChannel) selectedKey.channel();

                        String sendKey = getClientKey(clientChannel);
                        // 通知所有注册的客户端
                        for (Map.Entry<String, SocketChannel> entry : clientsMap.entrySet()) {
                            SocketChannel eachClient = entry.getValue();
                            ByteBuffer broadcastMsg = ByteBuffer.allocate(1024);
                            broadcastMsg.put((sendKey + ":" + selectedKey.attachment()).getBytes());
                            broadcastMsg.flip();
                            eachClient.write(broadcastMsg);

                        }
                        selectedKey.interestOps(SelectionKey.OP_READ);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            selectionKeys.clear();
        }
    }

    /**
     * 根据通道找到对应客户端的key
     *
     * @param clientChannel 通道
     * @return
     */
    public static String getClientKey(SocketChannel clientChannel) {
        String sendKey = null;

        for (Map.Entry<String, SocketChannel> entry : clientsMap.entrySet()) {
            if (clientChannel == entry.getValue()) {

                sendKey = entry.getKey();
                break;
            }
        }
        return sendKey;
    }

}
