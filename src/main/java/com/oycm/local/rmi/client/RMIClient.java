package com.oycm.local.rmi.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RMIClient {
    /**
     *
     * @param serviceInterface 远程注册的接口名
     * @param addr socket连接地址
     * @return 调用
     * @param <T> 客户端调用远程的代理对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getRemoteProxyObj(Class serviceInterface,
                                          InetSocketAddress addr) {

        /**
         * 返回动态代理 远程接口的对象
         * 注意: 这里只使用了远程接口的方法信息，并没有用到目标实际对象
         */
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(),
                new Class<?>[]{serviceInterface}, new InvocationHandler() {

                    // proxy:代理的对象, method：哪个方法（sayHi(参数列表)）, args:参数列表
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args)
                            throws Throwable {
                        // Socket建立连接
                        Socket socket = new Socket();
                        ObjectOutputStream output = null;
                        ObjectInputStream input = null;
                        try {
                            //addr包含了要访问的服务端的Ip和端口
                            socket.connect(addr);
                            //通过序列化流（对象流）向服务端发送请求
                            output = new ObjectOutputStream(socket.getOutputStream());
                            //发送请求的接口名
                            output.writeUTF(serviceInterface.getName());
                            //发送请求的方法名
                            output.writeUTF(method.getName());
                            //发送请求的方法的参数的类型
                            output.writeObject(method.getParameterTypes());
                            //发送请求的方法的参数值
                            output.writeObject(args);

                            //等待服务端处理的返回只
                            input = new ObjectInputStream(socket.getInputStream());
                            return input.readObject();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        } finally {
                            try {
                                if (output != null) {
                                    output.close();
                                }
                                if (input != null) {
                                    input.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}

