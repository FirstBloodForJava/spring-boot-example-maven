package com.oycm.controller;

import com.oycm.utils.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author ouyangcm
 * create 2025/1/10 16:36
 */
@RestController
public class DownloadController {

    Log log = LogFactory.getLog(DownloadController.class);

    @GetMapping(value = "/download-file/{fileId}.{fileType}")
    public void downloadFile(@PathVariable String fileId,
                             @PathVariable String fileType,
                             HttpServletResponse response) throws IOException {

        String fileContent = FileUtils.downloadFile("static", fileId, fileType);

        File www = new File("image");
        if (www.exists()) {
            if (www.isDirectory()) {
                String[] list = www.list();
                if (list != null) {
                    for (String s : list) {
                        log.info(s);
                        FileUtils.downloadFile("image", s);
                    }
                }

            }
        }

        FileUtils.print(response.getOutputStream(), fileContent);
    }
}
