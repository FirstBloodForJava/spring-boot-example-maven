package com.oycm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KafkaListenerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaListenerService.class);

    /*@KafkaListener(topics = "kRequest")
    public String listenAndReply(@Payload String message,
                                 @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId,
                                 @Header(KafkaHeaders.REPLY_TOPIC) String replyTopic) {
        logger.info("message: {}", message);
        logger.info("correlationId: {}", new String(correlationId));
        logger.info("replyTopic: {}", replyTopic);

        // 处理消息并返回响应
        return message.toUpperCase();
    }*/
}
