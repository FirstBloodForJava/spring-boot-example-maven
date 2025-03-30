package com.oycm.local.io.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockServer {

    public static void main(String[] args) throws IOException {

        try (ServerSocket server = new ServerSocket(8080)) {

            while (true) {
                // 一直阻塞，直到有客户端发来连接
                Socket socket = server.accept();

                //创建一个线程，用于给该客户端发送一个文件
                new Thread(new SendFile(socket)).start();
            }
        }finally {
            System.out.println("程序退出");
        }

    }

}
class SendFile implements Runnable{
    private Socket socket ;
    public SendFile(Socket socket) {
        this.socket = socket ;
    }
    @Override
    public void run() {
        File file  = new File("classpath:\\static\\favicon.ico");
        // 读取本地classpath路径文件
        try (InputStream fileIn = SendFile.class.getClassLoader().getResourceAsStream(".\\static\\favicon.ico")) {
            System.out.println("连接成功！");
            OutputStream out = socket.getOutputStream() ;


            byte[] bs = new byte[64] ;
            int len = -1 ;
            while( (len=fileIn.read(bs)) !=-1   ) {
                out.write(bs,0,len);
            }
            fileIn.close();
            out.close();
            socket.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
