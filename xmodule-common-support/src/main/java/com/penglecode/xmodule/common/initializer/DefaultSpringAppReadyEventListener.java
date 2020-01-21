package com.penglecode.xmodule.common.initializer;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 默认的{@link #ApplicationReadyEvent}事件监听器，用于在此时机做一些初始化操作
 * 
 * @author 	pengpeng
 * @date	2019年6月13日 下午2:04:54
 */
@Component
public class DefaultSpringAppReadyEventListener extends AbstractSpringBootEventListener<ApplicationReadyEvent> {

	@Override
	protected ConfigurableApplicationContext getApplicationContext(ApplicationReadyEvent event) {
		return event.getApplicationContext();
	}

}
