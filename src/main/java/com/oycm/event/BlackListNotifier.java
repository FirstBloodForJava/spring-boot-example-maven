package com.oycm.event;

import com.oycm.utils.JsonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class BlackListNotifier implements ApplicationListener<BlackListEvent> {

    private static Log log = LogFactory.getLog(BlackListNotifier.class);
    private String notificationAddress;

    public void setNotificationAddress(String notificationAddress) {
        this.notificationAddress = notificationAddress;
    }

    public void onApplicationEvent(BlackListEvent event) {
        log.info("receive Event");

        log.info(JsonUtils.objToString(event));
    }
}
