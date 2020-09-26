package com.penglecode.xmodule.master4j.springboot.springfactories.listeners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.context.ApplicationListener;

/**
 * ApplicationStartingEvent事件
 *
 * @see SpringApplication#run(java.lang.String...)
 * @see EventPublishingRunListener#starting()
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 9:44
 */
public class ApplicationStartingEventListener implements ApplicationListener<ApplicationStartingEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        System.out.println(String.format("【%s】>>> SpringBoot Application Starting ...", ApplicationStartingEvent.class.getSimpleName()));
    }
}
