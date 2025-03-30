package com.oycm.local.rmi.server;

/**
 * 服务端对外提供的借接口 客户端需要同样的备份
 */
public interface RMIService {
    String sayHi(String name) ;
}
