package com.oycm.config;

import com.oycm.entity.Customer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ouyangcm
 * create 2024/12/13 15:46
 */
@ConditionalOnBean(Customer.class)
@Configuration(proxyBeanMethods = false)
public class ConfigurationBeanTest {

    private static final Log log = LogFactory.getLog(ConfigurationBeanTest.class);

    static {
        log.info("类加载");
    }
    ConfigurationBeanTest() {
        log.info("ConfigurationBeanTest 构造方法执行");
    }

    @Bean
    public Customer customer() {
        log.info("Customer 构造方法执行");
        return new Customer();
    }
}
