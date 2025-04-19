package com.oycm.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.time.Duration;

@Configuration
public class ReplyKafkaConfig {

    private static final Logger logger = LoggerFactory.getLogger(ReplyKafkaConfig.class);

    // Topic 创建
    @Bean
    public NewTopic kRequest() {
        return TopicBuilder.name("request")
                .partitions(5)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic kReply() {
        return TopicBuilder.name("reply")
                .partitions(5)
                .replicas(1)
                .build();
    }


    // 创建Kafka消费者
    @Bean
    public ConcurrentMessageListenerContainer<String, String> repliesContainer(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {

        ConcurrentMessageListenerContainer<String, String> repliesContainer =
                containerFactory.createContainer("reply");
        repliesContainer.getContainerProperties().setGroupId("replyGroup");
        repliesContainer.setAutoStartup(false);
        return repliesContainer;
    }

    // 创建 ReplyingKafkaTemplate
    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingTemplate(
            ProducerFactory<String, String> pf,
            ConcurrentMessageListenerContainer<String, String> repliesContainer) {
        ReplyingKafkaTemplate<String, String, String> replyTemplate = new ReplyingKafkaTemplate<>(pf, repliesContainer);
        replyTemplate.setDefaultReplyTimeout(Duration.ofSeconds(30));
        return replyTemplate;
    }


}
