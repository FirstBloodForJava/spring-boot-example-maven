package com.oycm.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

/**
 * @author ouyangcm
 * 为了支持 JNDI，不起作用
 * create 2024/12/11 15:28
 */
// @Configuration(proxyBeanMethods = false)
public class TomcatJndiConfig {

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
                tomcat.enableNaming();
                return super.getTomcatWebServer(tomcat);
            }

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
        };
        return tomcat;
    }


}
