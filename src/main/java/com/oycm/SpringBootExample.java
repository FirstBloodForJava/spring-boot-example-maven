package com.oycm;


import com.oycm.dao.CustomerRepository;
import com.oycm.entity.Customer;
import com.oycm.utils.JsonUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

@RestController
@SpringBootApplication()
@ConfigurationPropertiesScan("com.oycm.config")
@EntityScan("com.oycm.entity")
public class SpringBootExample {

    private static final Logger log = LoggerFactory.getLogger(SpringBootExample.class);

    @Value("${name}")
    private String name;

    @Autowired
    private KafkaTemplate<String, String> template;

    private final CountDownLatch latch = new CountDownLatch(3);

    @Bean
    CommandLineRunner myCommandLineRunner(CustomerRepository repository) {

        return (String... args) -> {
            // 新增 4
            repository.save(new Customer("ouyangcm", "欧阳诚明"));
            repository.save(new Customer("tangdh", "唐狄豪"));
            repository.save(new Customer("zhangzx", "张臻祥"));
            repository.save(new Customer("fangyc", "方越程"));

            // 查询所有
            log.info("Customers found with findAll():");
            repository.findAll().forEach(customer -> {
                log.info(JsonUtils.objToString(customer));
            });

            // 向kafka发送消息
            //template.send("org.test1", "foo1");
            //template.send("org.test1", "foo2");
            //template.send("org.test1", "foo3");

            // 等待直到 计数器为0
            //latch.await(60, TimeUnit.SECONDS);
            log.info("Runner exec success");

        };
    }

    // 消费者
    @KafkaListener(topics = "org.test1")
    public void listen(ConsumerRecord<String, String> cr) throws Exception {
        log.info("consumer: " + cr.toString());
        latch.countDown();
    }

    @RequestMapping("/")
    String home() {
        log.info("");
        log.info("Hello World!" + name);
        return "Hello ! ";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootExample.class, args);
    }


}
