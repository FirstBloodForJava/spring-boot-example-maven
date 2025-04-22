package com.oycm.controller;

import com.oycm.utils.SSHUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ouyangcm
 * create 2025/4/22 13:38
 */
@RestController
public class LinuxExecController {

    private static final Logger logger = LoggerFactory.getLogger(LinuxExecController.class);

    @GetMapping("/exec")
    public String exec(@RequestParam String exec) throws Exception {
        logger.info("exec: {}", exec);

        String[] split = exec.split(" ");

        return SSHUtils.execSsh(split) == 0 ? "success" : "fail";
    }
}
