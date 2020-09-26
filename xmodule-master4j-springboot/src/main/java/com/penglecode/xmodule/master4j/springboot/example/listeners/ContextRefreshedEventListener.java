package com.penglecode.xmodule.master4j.springboot.example.listeners;

import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * ContextRefreshedEvent事件示例
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 17:52
 */
@Component
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        System.out.println(String.format("【%s】>>> %s refreshed, applicationContext = %s", ContextRefreshedEvent.class.getSimpleName(), applicationContext.getClass().getSimpleName(), applicationContext));
    }
}
