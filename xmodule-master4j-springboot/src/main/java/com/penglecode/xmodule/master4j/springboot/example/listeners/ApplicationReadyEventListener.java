package com.penglecode.xmodule.master4j.springboot.example.listeners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * ApplicationReadyEvent示例，在SpringBoot程序完全启动完毕后执行
 *
 *
 * @see EventPublishingRunListener#started(..)
 * @see SpringApplication#run(String...)
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 20:01
 */
@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        System.out.println(String.format("【%s】>>> %s is ready to serve, applicationContext = %s", ApplicationReadyEvent.class.getSimpleName(), applicationContext.getEnvironment().getProperty("spring.application.name"), applicationContext));
    }
}
