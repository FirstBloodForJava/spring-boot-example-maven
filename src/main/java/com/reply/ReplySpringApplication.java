package com.reply;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;

@SpringBootApplication
public class ReplySpringApplication {

    private static final Logger logger = LoggerFactory.getLogger(ReplySpringApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ReplySpringApplication.class, "--spring.profiles.active=reply");
    }

    @KafkaListener(topics = "request")
    @SendTo
    public String listen(String in) {
        logger.info("request: message: {}", in);
        return in.toUpperCase();
    }
}
