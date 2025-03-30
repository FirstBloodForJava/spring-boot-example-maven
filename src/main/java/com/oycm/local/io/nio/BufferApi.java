package com.oycm.local.io.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BufferApi {

    public static void main(String[] args) {


    }

    /**
     * ByteBuffer 方法使用
     */
    public static void useByteBuffer() {

        // 创建字符缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(100);

        // 添加数据
        buffer.put("oycm".getBytes());

        // 读取数据
        System.out.println(buffer.get());

        // 获取position到limit之间buffer数据
        ByteBuffer sliceBuffer = buffer.slice();

        // 判断缓冲区是否有剩余数据
        if (buffer.hasRemaining()) {
            // 缓冲区 剩余数量
            System.out.println(buffer.remaining());
        }

    }

    /**
     * 使用 Channel 复制文件
     *
     * @param sourcePath 复制源路径
     * @param targetPath 目标路径
     * @throws IOException
     */
    public static void copy(String sourcePath, String targetPath) throws IOException {
        long start = System.currentTimeMillis();
        FileInputStream input = new FileInputStream(sourcePath);
        FileOutputStream out = new FileOutputStream(targetPath);
        // 获取通道
        FileChannel inChannel = input.getChannel();
        FileChannel outChannel = out.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (inChannel.read(buffer) != -1) {
            // 写转读
            buffer.flip();
            outChannel.write(buffer);
            buffer.clear();
        }
        outChannel.close();
        inChannel.close();
        out.close();
        input.close();

        System.out.println("复制耗时(毫秒)：" + (System.currentTimeMillis() - start));
    }

    /**
     * 使用直接缓冲区复制文件
     *
     * @throws IOException
     */
    public static void copyByDirectMemory(String sourcePath, String targetPath) throws IOException {
        long start = System.currentTimeMillis();
        FileInputStream input = new FileInputStream(sourcePath);
        FileOutputStream out = new FileOutputStream(targetPath);
        // 获取通道
        FileChannel inChannel = input.getChannel();
        FileChannel outChannel = out.getChannel();

        // 创建直接缓冲区
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        while (inChannel.read(buffer) != -1) {
            // 写转读
            buffer.flip();
            outChannel.write(buffer);
            buffer.clear();
        }
        outChannel.close();
        inChannel.close();
        out.close();
        input.close();

        System.out.println("直接缓冲区复制耗时(毫秒)：" + (System.currentTimeMillis() - start));
    }

    /**
     * 内存映射复制
     * @param sourcePath
     * @param targetPath
     * @throws IOException
     */
    public static void copyByMemoryMapping(String sourcePath, String targetPath) throws IOException {
        long start = System.currentTimeMillis();
        // 使用文件的输入通道
        FileChannel inChannel = FileChannel.open(Paths.get(sourcePath), StandardOpenOption.READ);
        // 使用文件的输出通道
        FileChannel outChannel = FileChannel.open(Paths.get(targetPath), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);

        // 输入通道和输出通道之间的内存映射文件（内存映射文件处于堆外内存中）
        MappedByteBuffer inMappedBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMappedBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        //直接对内存映射文件进行读写
        byte[] dst = new byte[inMappedBuf.limit()];
        inMappedBuf.get(dst);
        outMappedBuf.put(dst);
        inChannel.close();
        outChannel.close();

        System.out.println("：" + ( System.currentTimeMillis() - start));
    }

    /**
     * 零拷贝：用户空间和内核空间之间的复制次数为0
     * @param sourcePath
     * @param targetPath
     * @throws IOException
     */
    public static void zeroCopy(String sourcePath, String targetPath) throws IOException{
        long start = System.currentTimeMillis();
        FileChannel inChannel = FileChannel.open(Paths.get(sourcePath), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get(targetPath), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);

        // 输入管道完成复制
        inChannel.transferTo(0, inChannel.size(), outChannel);

        // 输出通道完成复制,和上面等价
        //outChannel.transferFrom(inChannel, 0, inChannel.size());

        inChannel.close();
        outChannel.close();

        System.out.println("复制操作消耗的时间（毫秒）："+(System.currentTimeMillis() - start));
    }

    /**
     * 管道Pipe: 两个线程之间单向传输数据时，可以使用管道
     * SinkChannel 向管道写数据
     * SourceChannel 从管道读取数据
     * @throws IOException
     */
    public static void usePipe() throws IOException{
        // 创建管道
        Pipe pipe = Pipe.open();

        ByteBuffer buf = ByteBuffer.allocate(1024);
        // 通过 SinkChannel ，向Pipe中写数据
        Pipe.SinkChannel  sinkChannel = pipe.sink();
        buf.put("oycm".getBytes());
        buf.flip();
        sinkChannel.write(buf);

        // 通过SourceChannel，从Pipe中读取数据
        Pipe.SourceChannel sourceChannel = pipe.source();
        buf.flip();
        int len = sourceChannel.read(buf);
        System.out.println( new String(buf.array(),0,len));

        sourceChannel.close();
        sinkChannel.close();
    }
}
