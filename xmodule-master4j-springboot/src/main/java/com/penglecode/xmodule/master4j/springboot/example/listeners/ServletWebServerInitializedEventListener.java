package com.penglecode.xmodule.master4j.springboot.example.listeners;

import org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * ServletWebServerInitializedEvent示例，
 * 这个事件springboot-2.3.0.RELEASE以前是放在应用上下文refresh()之后触发的，
 * 也就是说之前ServletWebServerInitializedEvent的触发时机要晚于ContextRefreshedEvent，
 * 现在在springboot-2.3.0.RELEASE以后恰恰在ContextRefreshedEvent之前触发。
 *
 * 由代码可知，该事件是在WebServerStartStopLifecycle#start()的方法中触发的，即在Lifecycle#start()生命周期中触发的，
 * 此时当前示例Component已经注册到容器中去了，该示例正常生效，因为AbstractApplicationContext#registerListeners()调用在前，
 * AbstractApplicationContext#finishRefresh()(也就是getLifecycleProcessor().onRefresh())调用在后
 *
 *
 * https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.3-Release-Notes#webserverinitializedevent-and-contextrefreshedevent
 *
 * @see ServletWebServerApplicationContext#createWebServer()
 * @see ReactiveWebServerApplicationContext#createWebServer()
 * @see WebServerStartStopLifecycle#start()
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/25 17:34
 */
@Component
public class ServletWebServerInitializedEventListener implements ApplicationListener<ServletWebServerInitializedEvent> {
    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent event) {
        System.out.println(String.format("【%s】>>> Servlet WebServer Started during the applicationContext refreshing, applicationContext = %s", ServletWebServerInitializedEvent.class.getSimpleName(), event.getApplicationContext()));
    }
}
