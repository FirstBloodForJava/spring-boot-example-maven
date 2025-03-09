package com.oycm.utils;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaProducerClient {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "47.101.155.205:9092");
        properties.put("acks", "all");
        properties.put("linger.ms", 10);
        properties.put("retries", 1);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String,String> producer = new KafkaProducer<String, String>(properties);
        for (int i = 1; i < 2; i++) {
            System.out.println("开始: " + i);
            producer.send(new ProducerRecord<String, String>("org.test2", Integer.toString(i), Integer.toString(i)));
            System.err.println(i);
        }
        producer.close();
    }
}
