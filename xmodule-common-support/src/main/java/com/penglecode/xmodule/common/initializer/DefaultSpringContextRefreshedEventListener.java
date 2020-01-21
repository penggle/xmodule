package com.penglecode.xmodule.common.initializer;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 默认的{@link #ContextRefreshedEvent}事件监听器，用于在此时机做一些初始化操作
 * 
 * @author 	pengpeng
 * @date	2019年6月13日 下午2:04:54
 */
@Component
public class DefaultSpringContextRefreshedEventListener extends AbstractSpringContextEventListener<ContextRefreshedEvent> {

	@Override
	protected ConfigurableApplicationContext getApplicationContext(ContextRefreshedEvent event) {
		return (ConfigurableApplicationContext) event.getApplicationContext();
	}

}
