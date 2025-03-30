package com.oycm.local.rmi.test;


import com.oycm.local.rmi.client.RMIClient;
import com.oycm.local.rmi.server.RMIService;
import java.net.InetSocketAddress;


public class TestRMIClient {
    public static void main(String[] args) throws ClassNotFoundException {
        // 调用远程的com.oycm.local.rmi.server.RMIService接口，并执行接口中的sayHi()方法
        RMIService service = RMIClient.getRemoteProxyObj(
                Class.forName("com.oycm.local.rmi.server.RMIService" ) ,
                new InetSocketAddress("127.0.0.1", 9999)) ;
        System.out.println( service.sayHi("oycm1")  ) ;
    }
}
