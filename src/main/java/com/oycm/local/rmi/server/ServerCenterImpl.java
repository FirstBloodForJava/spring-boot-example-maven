package com.oycm.local.rmi.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerCenterImpl implements ServerCenter {


    /**
     * 对外提供调用的服务名称缓冲
     */
    private static HashMap<String, Class<?>> serviceRegister = new HashMap<>();
    /**
     * 服务端端口号
     */
    private int port;

    /**
     * 核心线程数和最大线程数一样,队列使用最大容量
     */
    private static ExecutorService executor
            = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * 服务是否启动
     */
    private static volatile boolean isRunning = false;

    public ServerCenterImpl(int port) {
        this.port = port;
    }

    /**
     * 启动服务端 通过ServerSocket和客户端通信
     */
    @Override
    public void start() {
        ServerSocket server = null;
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress(port));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        isRunning = true;
        // 死循环等待客户端连接
        while (true) {

            System.out.println("服务已启动...");
            Socket socket = null;
            try {
                // 阻塞,等待客户端连接
                socket = server.accept();

                System.out.println("获取Socket连接: " + socket);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            // 启动一个线程 去处理客户请求
            executor.execute(new ServiceTask(socket));
        }
        // 异常关闭socket
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void stop() {
        isRunning = false;
        executor.shutdown();
    }

    @Override
    public void register(Class<?> service, Class<?> serviceImpl) {
        serviceRegister.put(service.getName(), serviceImpl);
    }

    //处理请求的线程
    private static class ServiceTask implements Runnable {
        private Socket socket;

        public ServiceTask() {
        }

        public ServiceTask(Socket socket) {
            this.socket = socket;
        }

        //具体的处理逻辑
        @Override
        public void run() {
            ObjectOutputStream output = null;
            ObjectInputStream input = null;
            try {
                //接收到客户端的各个请求参数（接口名、方法名、参数类型、参数值）
                input = new ObjectInputStream(socket.getInputStream());

                // 1.请求的接口名
                String serviceName = input.readUTF();
                // 2.请求的方法名
                String methodName = input.readUTF();
                // 3.请求方法的参数类型
                Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                // 4.请求方法的参数名
                Object[] arguments = (Object[]) input.readObject();

                // 根据客户请求，到服务注册中心map中找到与之对应的具体接口（即RMIService）
                Class<?> ServiceClass = serviceRegister.get(serviceName);
                // 反射获取要调用的方法
                Method method = ServiceClass.getMethod(methodName, parameterTypes);
                // 执行该方法(反射创建对象,调用方法)
                Object result = method.invoke(ServiceClass.newInstance(), arguments);

                // 将执行完毕的返回值，返回给客户端
                output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }


}
