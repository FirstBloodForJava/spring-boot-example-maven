package com.oycm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;

/**
 * @author ouyangcm
 * create 2025/1/10 16:27
 */
public class FileUtils {

    private static Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     *
     * @param fileTree 目录
     * @param fileId 文件名
     * @param fileType 文件类型
     * @return
     */
    public static String downloadFile(String fileTree, String fileId, String fileType) {
        return downloadFile(fileTree, fileId + "." + fileType);
    }

    public static String downloadFile(String fileTree, String fileName) {
        File file = new File(String.format("%s/%s", fileTree, fileName));

        if (!file.exists()) {

            URL url = FileUtils.class.getClassLoader().getResource(String.format("%s/%s", fileTree, fileName));

            try {
                assert url != null;
                log.info(url.toURI().toString());
                file = new File(url.toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            if (!file.exists()) {
                throw new RuntimeException(file.getPath() + " classpath中也不存在");
            }


        }

        String fileContent;
        try {
            InputStream input = Files.newInputStream(file.toPath());
            if (fileName.contains("png")) return "";
            byte[] bytes = convertToByteArray(input);
            fileContent = Base64.getEncoder().encodeToString(bytes);

        } catch (Exception e) {
            log.error("访问文件失败", e);
            throw new RuntimeException("文件不存在");
        }

        return fileContent;
    }

    public static byte[] convertToByteArray(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 1024)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }

    public static void print (OutputStream outputStream, String fileContent) throws IOException {
        byte[] imgBuffer = Base64.getDecoder().decode(fileContent);
        outputStream.write(imgBuffer);
        outputStream.flush();
        outputStream.close();
    }

}
