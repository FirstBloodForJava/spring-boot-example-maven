package com.oycm.local.io.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileLockDemo {

    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        String filePath = "";
        //r读 w写 rw读写
        RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
        FileChannel fileChannel = raf.getChannel();
        /*
            将abc.txt中position=2，size=4的内容加锁（即只对文件的部分内容加了锁）。
            lock()第三个布尔参数的含义如下：
                true:共享锁。实际上是指“读共享”：某一线程将资源锁住之后，其他线程既只能读、
不能写该资源。
                false:独占锁。某一线程将资源锁住之后，其他线程既不能读、也不能写该资源。
         */
        /**
         * 对文件部分内容加锁, position=, size=4
         * true:共享锁。实际上是指“读共享”：某一线程将资源锁住之后，其他线程既只能读、不能写该资源。
         * false:独占锁。某一线程将资源锁住之后，其他线程既不能读、也不能写该资源。
         */
        FileLock fileLock = fileChannel.lock(2, 4, true);
        System.out.println("main线程将" + filePath + "锁3秒...");

        new Thread(
                () -> {
                    try {
                        byte[] bs = new byte[8];
                        // 新线程 读取文件
                        raf.read(bs,0,8);
                        // 新线程 往文件写入
                        // fileLock.isValid(); 可以判断访问的资源是否被加锁
                        raf.write("helloWorld".getBytes(),0,8);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }).start();

        Thread.sleep(3000);
        System.out.println("3秒结束，main释放锁");
        fileLock.release();
    }

}
