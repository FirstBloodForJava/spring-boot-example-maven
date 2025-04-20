package com.oycm.api;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Properties;
import java.util.regex.Pattern;

public class KafkaConsumerClient {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "47.101.155.205:9092");
        properties.put("group.id", "spring-kafka");
        properties.put("auto.offset.reset", "earliest");

        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(Pattern.compile("org.test1"));

        ConsumerRecords<String, String> msList = consumer.poll(Duration.ofSeconds(1));

        System.out.println("size: " + msList.count());

        for (ConsumerRecord<String, String> message : msList) {
            System.out.println(Thread.currentThread().getName() + ": " + message.toString());
        }

        consumer.close();
        System.out.println("consumer close");
    }
}
