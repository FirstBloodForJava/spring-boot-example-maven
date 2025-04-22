package com.oycm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication()
public class SpringBootExample {

    private static final Log log = LogFactory.getLog(SpringBootExample.class);
    @Value("${name}")
    private String name;


    @RequestMapping("/")
    String home() {
        log.info("Hello World!" + name);
        return "HelloWorld";
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootExample.class, args);

        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();


    }


}
