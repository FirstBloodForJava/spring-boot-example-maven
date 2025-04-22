package com.oycm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author ouyangcm
 * create 2025/4/22 13:32
 */
public class SSHUtils {

    private static final Logger logger = LoggerFactory.getLogger(SSHUtils.class);

    public static int execSsh(String[] exec) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(exec);

        // 合并错误流到标准输出
        processBuilder.redirectErrorStream(true);

        // 启动进程
        Process process = processBuilder.start();

        // 读取命令输出
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            logger.info("exec: " + line);
        }

        // 等待命令执行完成并获取退出码
        int exitCode = process.waitFor();
        logger.info("exec result Code: {}", exitCode);

        // 根据退出码判断是否成功
        if (exitCode == 0) {
            logger.info("success");
        } else {
            logger.warn("fail");
        }
        return exitCode;
    }

}
