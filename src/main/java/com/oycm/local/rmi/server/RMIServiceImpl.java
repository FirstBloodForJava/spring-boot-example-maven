package com.oycm.local.rmi.server;

/**
 * 服务端接口的具体实现,客户端不需要实现
 */
public class RMIServiceImpl implements RMIService {
    @Override
    public String sayHi(String name) {
        return "hi," + name ;
    }
}
