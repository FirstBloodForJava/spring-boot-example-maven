package com.oycm.service;

import com.oycm.utils.JsonUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ReplyKafkaService {
    private static final Logger logger = LoggerFactory.getLogger(ReplyKafkaService.class);
    private static final AtomicInteger count = new AtomicInteger();
    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate = null;
    private final KafkaTemplate<String,String> kafkaTemplate;

    public ReplyKafkaService(KafkaTemplate<String,String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String sendAndReceive(String message) throws Exception{

        // 创建消息
        ProducerRecord<String, String> record = new ProducerRecord<>("request", message);

        // todo 可以去掉 两个头，这个会根据发送者的配置自动添加
        // 添加回复的主题
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "reply".getBytes()));
        // 关联id
        record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, record.value().getBytes()));

        // 发送
        RequestReplyFuture<String, String, String> replyFuture = replyingKafkaTemplate.sendAndReceive(record);

        // 获取发送结果
        SendResult<String, String> sendResult = replyFuture.getSendFuture().get(10, TimeUnit.SECONDS);
        logger.info("Sent ok: " + sendResult.getRecordMetadata());
        logger.info("Sent json: " + JsonUtils.objToString(sendResult.getRecordMetadata()));

        // 获取回答结果
        ConsumerRecord<String, String> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);
        logger.info("Return value: " + consumerRecord.value());

        return consumerRecord.value();
    }

    public String send(String message) {

        ProducerRecord<String,String> record = new ProducerRecord("org.test1", count.getAndIncrement() + "", message);

        kafkaTemplate.send(record);

        return "success";
    }

}
