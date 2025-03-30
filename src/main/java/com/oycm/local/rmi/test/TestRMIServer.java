package com.oycm.local.rmi.test;

import com.oycm.local.rmi.server.RMIService;
import com.oycm.local.rmi.server.RMIServiceImpl;
import com.oycm.local.rmi.server.ServerCenter;
import com.oycm.local.rmi.server.ServerCenterImpl;

public class TestRMIServer {
    public static void main(String[] args) {
        //用线程的形式启动服务
        new Thread(new Runnable() {
            @Override
            public void run() {
                //服务中心
                ServerCenter server = new ServerCenterImpl(9999);
                //将RMIService接口及实现类，注册到服务中心
                server.register(RMIService.class, RMIServiceImpl.class);
                server.start();
            }
        }).start();
    }
}
