package com.oycm.local.io.bio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class BlockClient {

    public static void main(String[] args) throws  IOException {

        Socket socket = new Socket("127.0.0.1",8080);
        // 接受服务端发来到文件
        InputStream in = socket.getInputStream() ;
        byte[] bs = new byte[64] ;
        int len = -1 ;
        OutputStream fileOut = new FileOutputStream("favicon.ico") ;
        while( (len =in.read(bs))!=-1 ) {
            fileOut.write(bs,0,len);
        }
        System.out.println("文件接收成功！");
        fileOut.close();
        in.close();
        socket.close();
    }

}
