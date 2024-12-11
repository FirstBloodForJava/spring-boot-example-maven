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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@ConfigurationPropertiesScan("com.oycm.config")
@EntityScan("com.oycm.entity")
public class SpringBootExample {

    private static final Log log = LogFactory.getLog(SpringBootExample.class);
    @Value("${name}")
    private String name;

    @Autowired
    private AcmeProperties acmeProperties;

    @RequestMapping("/")
    AcmeProperties home() {
        log.info(JsonUtils.objToString(acmeProperties));
        log.info("Hello World!" + name);
        return acmeProperties;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootExample.class, args);

        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();

        System.out.println(beanFactory.containsBean("acme-" + AcmeProperties.class.getName()));

    }


}
