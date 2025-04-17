package com.oycm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
@ConfigurationPropertiesScan("com.oycm.config")
@EntityScan("com.oycm.entity")
public class SpringBootExample {

    private static final Logger log = LoggerFactory.getLogger(SpringBootExample.class);

    @Value("${name}")
    private String name;


    @RequestMapping("/")
    String home() {
        log.info("");
        log.info("Hello World!" + name);
        return "Hello ! ";
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootExample.class, args);

        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();

    }


}
