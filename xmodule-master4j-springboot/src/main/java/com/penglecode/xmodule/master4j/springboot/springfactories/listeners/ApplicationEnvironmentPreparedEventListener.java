package com.penglecode.xmodule.master4j.springboot.springfactories.listeners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * ApplicationEnvironmentPreparedEvent事件示例
 *
 * @see SpringApplication#prepareEnvironment(..)
 * @see EventPublishingRunListener#environmentPrepared()
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 9:44
 */
public class ApplicationEnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        System.out.println(String.format("【%s】>>> SpringBoot ApplicationEnvironment Prepared, environment = %s", ApplicationEnvironmentPreparedEvent.class.getSimpleName(), environment));
    }
}
