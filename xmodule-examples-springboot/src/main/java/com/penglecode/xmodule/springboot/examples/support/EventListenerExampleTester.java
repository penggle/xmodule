package com.penglecode.xmodule.springboot.examples.support;

import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventListenerExampleTester {

	@EventListener(WebServerInitializedEvent.class)
	public void onWebServerInitializedEvent(WebServerInitializedEvent event) {
		System.out.println(">>> onWebServerInitializedEvent: " + event);
	}
	
	@EventListener(ContextRefreshedEvent.class)
	public void onContextRefreshedEvent(ContextRefreshedEvent event) {
		System.out.println(">>> onContextRefreshedEvent: " + event);
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReadyEvent(ApplicationReadyEvent event) {
		System.out.println(">>> onApplicationReadyEvent: " + event);
	}
	
	@EventListener(ApplicationStartedEvent.class)
	public void onApplicationStartedEvent(ApplicationStartedEvent event) {
		System.out.println(">>> onApplicationStartedEvent: " + event);
	}
	
	/**
	 * 由于该事件触发过早(早于ContextRefreshedEvent)，而那时@EventListener的处理器还未注册该事件
	 * 所以该事件监听无效
	 */
	@EventListener(ApplicationEnvironmentPreparedEvent.class)
	public void onApplicationEnvironmentPreparedEvent(ApplicationEnvironmentPreparedEvent event) {
		System.out.println(">>> onApplicationEnvironmentPreparedEvent: " + event);
	}
	
	/**
	 * 由于该事件触发过早(早于ContextRefreshedEvent)，而那时@EventListener的处理器还未注册该事件
	 * 所以该事件监听无效
	 */
	@EventListener(ApplicationContextInitializedEvent.class)
	public void onApplicationContextInitializedEvent(ApplicationContextInitializedEvent event) {
		System.out.println(">>> onApplicationContextInitializedEvent: " + event);
	}
	
}
