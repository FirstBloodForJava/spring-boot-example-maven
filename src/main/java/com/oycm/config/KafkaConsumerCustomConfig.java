package com.oycm.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ContainerCustomizer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
public class KafkaConsumerCustomConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerCustomConfig.class);

    static class ConsumerContainerCustomizer<K, V> implements ContainerCustomizer<K, V, ConcurrentMessageListenerContainer<K, V>> {

        @Override
        public void configure(ConcurrentMessageListenerContainer<K, V> container) {
            logger.info("custom ConcurrentMessageListenerContainer");
        }
    }
}
