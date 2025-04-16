package com.oycm;

import com.oycm.config.AcmeProperties;
import com.oycm.utils.JsonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@SpringBootApplication()
@ConfigurationPropertiesScan("com.oycm.config")
@EntityScan("com.oycm.entity")
public class SpringBootExample {

    private static final Log log = LogFactory.getLog(SpringBootExample.class);
    @Value("${name}")
    private String name;

    @Autowired
    private AcmeProperties acmeProperties;
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/")
    AcmeProperties home() {
        log.info(JsonUtils.objToString(acmeProperties));
        log.info("Hello World!" + name);
        List result = restTemplate.getForObject("http://localhost:8080/getCus", List.class);
        log.info(JsonUtils.objToString(result));
        return acmeProperties;
    }
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootExample.class,
                "--spring.application.name=frontServer",
                "--server.port=8080");

        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();

        System.out.println(beanFactory.containsBean("acme-" + AcmeProperties.class.getName()));

    }


}
