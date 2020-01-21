package com.penglecode.xmodule.common.initializer;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 默认的{@link #ApplicationStartedEvent}事件监听器，用于在此时机做一些初始化操作
 * 
 * @author 	pengpeng
 * @date	2019年6月13日 下午2:04:54
 */
@Component
public class DefaultSpringAppStartedEventListener extends AbstractSpringBootEventListener<ApplicationStartedEvent> {

	@Override
	protected ConfigurableApplicationContext getApplicationContext(ApplicationStartedEvent event) {
		return event.getApplicationContext();
	}
	
}
