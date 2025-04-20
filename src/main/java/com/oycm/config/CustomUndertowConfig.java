package com.oycm.config;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xnio.Options;

@Configuration
public class CustomUndertowConfig {

    @Bean
    public UndertowBuilderCustomizer myUndertowCustom() {
        return new CustomBuild();
    }

    static class CustomBuild implements UndertowBuilderCustomizer {

        @Override
        public void customize(Undertow.Builder builder) {

            // worker
            // 覆盖io线程数量配置
            builder.setWorkerOption(Options.WORKER_IO_THREADS, 4);

            // 修改线程名称前缀
            builder.setWorkerOption(Options.WORKER_NAME, "Undertow");

            // 修改工作线程数
            builder.setWorkerOption(Options.WORKER_TASK_MAX_THREADS, 200);
            builder.setWorkerOption(Options.WORKER_TASK_CORE_THREADS, 200);

            // Socket
            // 配置连接阈值 失败数量不稳定
            builder.setSocketOption(Options.CONNECTION_HIGH_WATER, 200);
            builder.setSocketOption(Options.CONNECTION_LOW_WATER, 100);

            //
            builder.setSocketOption(UndertowOptions.QUEUED_FRAMES_HIGH_WATER_MARK, 300);
            builder.setSocketOption(UndertowOptions.QUEUED_FRAMES_HIGH_WATER_MARK, 200);

            // 配置连接队列
            //builder.setSocketOption(Options.BACKLOG, 100);

            // 配置读写超时时间
            builder.setSocketOption(Options.READ_TIMEOUT, 10000);
            builder.setSocketOption(Options.WRITE_TIMEOUT, 10000);

            builder.setSocketOption(UndertowOptions.REQUEST_PARSE_TIMEOUT, 1000);

            // Server

            // TCP连接建立后，第一个HTTP请求到达前的空闲等待时间
            builder.setServerOption(UndertowOptions.NO_REQUEST_TIMEOUT, 30 * 1000);
            // 两个连续请求之间的空闲时间(Keep-Alive连接) 超过关闭连接
            builder.setServerOption(UndertowOptions.IDLE_TIMEOUT, 30 * 1000);

            // 并发请求配置
            //builder.setServerOption(UndertowOptions.MAX_CONCURRENT_REQUESTS_PER_CONNECTION, 5);
        }
    }
}
