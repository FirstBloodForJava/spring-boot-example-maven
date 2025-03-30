package com.oycm.local.io.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

public class AIOOperateFileDemo {

    // Future 模式：读
    public static void readByFuture(String filePath) throws Exception {
        Path path = Paths.get(filePath);
        AsynchronousFileChannel channel = AsynchronousFileChannel.open(path);

        // 定义一个buffer，用于存放文件的内容
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        /**
         *  read()作用: 将文件通过 channel 读入 buffer 中,从0开始
         *  read()是一个异步的方法:
         *      会开启一个新线程，并且在这个新线程中读取文件;
         *  如何判断线程将文件读取完毕?
         *      future.isDone()的返回值为true;
         *      future.get() 方法不在阻塞
         */
        Future<Integer> future = channel.read(buffer, 0);

        // false指向其它操作
        while (!future.isDone()) {
            System.out.println("在read()的同时，可以处理其他事情...");
        }

        Integer readNumber = future.get();
        buffer.flip();
        String data = new String(buffer.array(), 0, buffer.limit());
        System.out.println("read number:" + readNumber);
        System.out.println(data);
    }

    // 回调模式：读
    public static void readByCall(String filePath) throws Exception {
        Path path = Paths.get(filePath);
        AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 在read()方法将文件全部读取到buffer之前，main线程可以异步进行其他操作
        channel.read(buffer, 0, null, new CompletionHandler<Integer, ByteBuffer>() {
            // 完成回调
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                buffer.flip();
                String data = new String(buffer.array(), 0, buffer.limit());
                System.out.println(data);
                System.out.println("read()完毕！");
            }

            @Override
            public void failed(Throwable e, ByteBuffer attachment) {
                System.out.println("异常...");
            }
        });

        System.out.println("在read()完毕以前，可以异步处理其他事情...");
        Thread.sleep(1000);

    }

    // Future模式：写
    public static void writeByFuture(String filePath) throws Exception {
        Path path = Paths.get(filePath);
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        long position = 0;

        buffer.put("hello world".getBytes());
        buffer.flip();

        Future<Integer> future = fileChannel.write(buffer, position);
        buffer.clear();

        while (!future.isDone()) {
            System.out.println("other thing....");
        }
        Integer result = future.get();
        System.out.println("写完毕！共写入字节数：" + result);
    }

    // 回调模式：写
    public static void writeByCall(String filePath) throws Exception {

        Path path = Paths.get(filePath);
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("hello the world".getBytes());
        buffer.flip();
        fileChannel.write(buffer, 0, null, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("写完毕！共写入的字节数: " + result);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                System.out.println("发生了异常...");
            }
        });

        System.out.println("other things...");
        Thread.sleep(1000);

    }

}
