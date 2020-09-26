package com.penglecode.xmodule.master4j.springboot.springfactories.listeners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.context.ApplicationListener;

/**
 * ApplicationContextInitializedEvent事件示例
 *
 * @see SpringApplication#prepareContext(..)
 * @see EventPublishingRunListener#contextPrepared(..)
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 16:53
 */
public class ApplicationContextInitializedEventListener implements ApplicationListener<ApplicationContextInitializedEvent> {
    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        System.out.println(String.format("【%s】>>> SpringBoot Application Initialized(but not refreshed), applicationContext = %s", ApplicationContextInitializedEvent.class.getSimpleName(), event.getApplicationContext()));
    }
}
