package com.penglecode.xmodule.master4j.springboot.springfactories.listeners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.context.ApplicationListener;

/**
 * ApplicationPreparedEvent事件
 *
 * @see SpringApplication#prepareContext(..)
 * @see EventPublishingRunListener#contextLoaded(..)
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 16:36
 */
public class ApplicationPreparedEventListener implements ApplicationListener<ApplicationPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        System.out.println(String.format("【%s】>>> SpringBoot Application Prepared(but not refreshed), applicationContext = %s", ApplicationPreparedEvent.class.getSimpleName(), event.getApplicationContext()));
    }
}
