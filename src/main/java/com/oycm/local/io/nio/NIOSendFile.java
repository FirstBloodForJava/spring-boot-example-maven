package com.oycm.local.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NIOSendFile {

    public static void client(String filePath) throws IOException {
        FileChannel inFileChannel = FileChannel.open(Paths.get(filePath), StandardOpenOption.READ);
        // 创建与服务端建立连接的SocketChannel对象
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
        // 分配指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        long start = System.currentTimeMillis();
        // 读取本地文件，并发送到服务端
        while (inFileChannel.read(buffer) != -1) {
            buffer.rewind();
            socketChannel.write(buffer);
            buffer.clear();
        }
        inFileChannel.close();
        socketChannel.close();
        long end = System.currentTimeMillis();
        System.out.println("NIO客户端发送耗时: " + (end - start));
    }

    public static void server(String path) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        FileChannel outFileChannel = FileChannel.open(Paths.get(path), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        serverSocketChannel.bind(new InetSocketAddress(8080));
        // 创建与客户端建立连接的SocketChannel对象
        SocketChannel sChannel = serverSocketChannel.accept();
        System.out.println("连接成功...");

        long start = System.currentTimeMillis();
        // 分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        // 接收客户端发送的文件，并保存到本地
        while (sChannel.read(buf) != -1) {
            buf.flip();
            outFileChannel.write(buf);
            buf.clear();
        }
        System.out.println("接收成功！");

        sChannel.close();
        outFileChannel.close();
        serverSocketChannel.close();
        long end = System.currentTimeMillis();
        System.out.println("NIO服务端发送耗时：" + (end - start));
    }

    public static void directMemoryClient(String filePath) throws IOException {
        long start = System.currentTimeMillis();
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));

        FileChannel inFileChannel = FileChannel.open(Paths.get(filePath), StandardOpenOption.READ);
        // 通过inFileChannel.size()获取文件的大小，从而在内核地址空间中开辟与文件大小相同的直接缓冲区
        inFileChannel.transferTo(0, inFileChannel.size(), socketChannel);

        inFileChannel.close();
        socketChannel.close();
        long end = System.currentTimeMillis();
        System.out.println("NIO客户端发送耗时(直接内存): " + (end - start));
    }

    public static void main(String[] args) throws IOException {
        // 可以用线程启动
    }

}
