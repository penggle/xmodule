package com.penglecode.xmodule.master4j.springboot.example.listeners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * ApplicationStartedEvent示例，在SpringBoot程序完全启动完毕后执行
 *
 *
 * @see EventPublishingRunListener#started(..)
 * @see SpringApplication#run(java.lang.String...)
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 20:01
 */
@Component
public class ApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        System.out.println(String.format("【%s】>>> %s has already started, applicationContext = %s", ApplicationStartedEvent.class.getSimpleName(), event.getSpringApplication().getMainApplicationClass().getSimpleName(), applicationContext));
    }
}
