package com.oycm.config;

/**
 * @author ouyangcm
 * 为了支持 JNDI，不起作用
 * create 2024/12/11 15:28
 */
//@Configuration(proxyBeanMethods = false)
//public class TomcatJndiConfig {
//
//    @Bean
//    public ServletWebServerFactory servletContainer(ObjectProvider<TomcatConnectorCustomizer> connectorCustomizers,
//                                                    ObjectProvider<TomcatContextCustomizer> contextCustomizers,
//                                                    ObjectProvider<TomcatProtocolHandlerCustomizer<?>> protocolHandlerCustomizers) {
//        TomcatServletWebServerFactory tomcat = new MyTomcatServletWebServerFactory();
//        tomcat.getTomcatConnectorCustomizers()
//                .addAll(connectorCustomizers.orderedStream().collect(Collectors.toList()));
//        tomcat.getTomcatContextCustomizers()
//                .addAll(contextCustomizers.orderedStream().collect(Collectors.toList()));
//        tomcat.getTomcatProtocolHandlerCustomizers()
//                .addAll(protocolHandlerCustomizers.orderedStream().collect(Collectors.toList()));
//        return tomcat;
//    }
//
//    static class MyTomcatServletWebServerFactory extends TomcatServletWebServerFactory {
//        @Override
//        protected void postProcessContext(Context context) {
//            ContextResource resource = new ContextResource();
//            resource.setName("MyDataSource");
//            resource.setType(HikariDataSource.class.getName());
//            resource.setProperty("url", "jdbc:oracle:thin:@//192.168.130.109:1521/testeir");
//            resource.setProperty("username", "LGSA_EP");
//            resource.setProperty("password", "easipass");
//            context.getNamingResources().addResource(resource);
//            super.postProcessContext(context);
//        }
//    }
//
//}
