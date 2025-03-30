package com.oycm.local.io.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 客户端
 */
public class ChatClient {

    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            // 切换到非阻塞模式
            socketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            // 在客户端的选择器上，注册一个通道，并标识该通道所感兴趣的事件是：向服务端发送连接（连接就绪）。对应于服务端的OP_ACCEPT事件
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            // 服务端端口 8081, 8082, 8083
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8081));
            while (true) {
                selector.select();
                // selectionKeys 包含了所有通道与选择器之间的关系（请求连接、读、写）
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey selectedKey = keyIterator.next();
                    // 判断是否连接成功
                    if (selectedKey.isConnectable()) {
                        ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
                        // 创建一个用于和服务端交互的Channel
                        SocketChannel client = (SocketChannel) selectedKey.channel();
                        // 如果状态是：正在连接中...
                        if (client.isConnectionPending()) {

                            if (client.finishConnect()) {
                                System.out.println("连接成功！访问的端口是：" + 8081);
                                // 向服务端发送一条测试消息
                                sendBuffer.put("connecting".getBytes());
                                sendBuffer.flip();
                                client.write(sendBuffer);
                            }

                            // 单独的线程向服务端发送消息
                            new Thread(() -> {
                                while (true) {
                                    try {
                                        sendBuffer.clear();
                                        //接收用户从控制台输入的内容，并发送给服务端
                                        InputStreamReader reader = new InputStreamReader(System.in);
                                        BufferedReader bReader = new BufferedReader(reader);
                                        String message = bReader.readLine();

                                        sendBuffer.put(message.getBytes());
                                        sendBuffer.flip();
                                        client.write(sendBuffer);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                        // 标记通道感兴趣的事件是：读取服务端消息（读就绪）
                        client.register(selector, SelectionKey.OP_READ);

                    } else if (selectedKey.isReadable()) {
                        // 读就绪
                        SocketChannel client = (SocketChannel) selectedKey.channel();
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                        int len = client.read(readBuffer);
                        if (len > 0) {
                            String receive = new String(readBuffer.array(), 0, len);
                            System.out.println("客户端接收消息: " + receive);
                        }
                    }
                }
                selectionKeys.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
