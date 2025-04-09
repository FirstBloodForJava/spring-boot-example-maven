package com.oycm.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import java.util.stream.Collectors;

/**
 * @author ouyangcm
 * 为了支持 JNDI，不起作用
 * create 2024/12/11 15:28
 */
//@Configuration(proxyBeanMethods = false)
public class TomcatJndiConfig {

    @Bean
    public ServletWebServerFactory servletContainer(ObjectProvider<TomcatConnectorCustomizer> connectorCustomizers,
                                                    ObjectProvider<TomcatContextCustomizer> contextCustomizers,
                                                    ObjectProvider<TomcatProtocolHandlerCustomizer<?>> protocolHandlerCustomizers) {
        TomcatServletWebServerFactory tomcat = new MyTomcatServletWebServerFactory();
        tomcat.getTomcatConnectorCustomizers()
                .addAll(connectorCustomizers.orderedStream().collect(Collectors.toList()));
        tomcat.getTomcatContextCustomizers()
                .addAll(contextCustomizers.orderedStream().collect(Collectors.toList()));
        tomcat.getTomcatProtocolHandlerCustomizers()
                .addAll(protocolHandlerCustomizers.orderedStream().collect(Collectors.toList()));
        return tomcat;
    }

    static class MyTomcatServletWebServerFactory extends TomcatServletWebServerFactory {
        @Override
        protected void postProcessContext(Context context) {
            ContextResource resource = new ContextResource();
            resource.setName("MyDataSource");
            resource.setType(HikariDataSource.class.getName());
            resource.setProperty("url", "jdbc:oracle:thin:@//192.168.130.109:1521/testeir");
            resource.setProperty("username", "LGSA_EP");
            resource.setProperty("password", "easipass");
            context.getNamingResources().addResource(resource);
            super.postProcessContext(context);
        }
    }

}
