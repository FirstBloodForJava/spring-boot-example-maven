package com.oycm.local.rmi.server;

/**
 * 服务端对外提供服务的抽象
 */
public interface ServerCenter {
    /**
     * 启动服务端
     */
    void start() ;

    /**
     * 停止服务
     */
    void stop();


    /**
     * 注册服务
     * @param service 接口类
     * @param serviceImpl 接口实现类
     */
    void register(Class<?> service, Class<?> serviceImpl);
}
