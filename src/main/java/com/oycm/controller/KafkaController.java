package com.oycm.controller;

import com.oycm.service.ReplyKafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);
    private final ReplyKafkaService replyKafkaService;

    public KafkaController (ReplyKafkaService replyKafkaService) {
        this.replyKafkaService = replyKafkaService;
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) throws Exception {
        logger.info("/send , message = {}", message);
        return replyKafkaService.sendAndReceive(message);
    }
}
