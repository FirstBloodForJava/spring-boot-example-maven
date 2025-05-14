package com.oycm.config.actuator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ouyangcm
 * create 2025/5/14 16:35
 */
@Configuration
public class InfoEndpointConfig {

    private static final Logger log = LoggerFactory.getLogger(InfoEndpointConfig.class);

    // 可去掉
    @Bean
    @ConditionalOnMissingBean
    public InfoEndpoint infoEndpoint(ObjectProvider<InfoContributor> infoContributors) {
        return new InfoEndpoint(infoContributors.orderedStream().collect(Collectors.toList()));
    }

    @Bean
    public CustomInfoContributor customInfoContributor() {
        log.info("new CustomInfoContributor");
        return new CustomInfoContributor();
    }

    // 自定义的 InfoContributor 实现
    static class CustomInfoContributor implements InfoContributor {

        @Override
        public void contribute(Info.Builder builder) {
            Map<String,String> properties = new HashMap<>();

            // 获取该 class 的版本
            properties.put("version", InfoContributor.class.getPackage().getImplementationVersion());
            properties.put("componentsName", "custom");

            builder.withDetail("custom", properties);
        }
    }
}
