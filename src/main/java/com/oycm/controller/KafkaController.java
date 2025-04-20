package com.oycm.controller;

import com.oycm.service.ReplyKafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class KafkaController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);
    private final ReplyKafkaService replyKafkaService;

    public KafkaController (ReplyKafkaService replyKafkaService) {
        this.replyKafkaService = replyKafkaService;

    }

    @GetMapping("/sendAndReceive")
    public String sendAndReceive(@RequestParam String message) throws Exception {
        logger.info("/sendAndReceive , message = {}", message);
        return replyKafkaService.sendAndReceive(message);
    }

    @GetMapping("/send")
    public String send(@RequestParam String message, HttpServletRequest httpRequest) throws Exception {
        logger.info("/send , message = {}", message);
        logger.info("remoteAddr: {}", httpRequest.getRemoteAddr());
        return replyKafkaService.send(httpRequest.getRemoteAddr() + "-" + message);
    }
}
