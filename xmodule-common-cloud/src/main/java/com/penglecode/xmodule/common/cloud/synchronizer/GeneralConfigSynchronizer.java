package com.penglecode.xmodule.common.cloud.synchronizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import com.penglecode.xmodule.common.initializer.SpringAppInitializer;

/**
 * 通用配置同步器
 * 
 * @author 	pengpeng
 * @date	2019年6月19日 下午2:09:55
 */
public abstract class GeneralConfigSynchronizer<T extends ApplicationEvent> implements SpringAppInitializer, ApplicationListener<T> {

	protected final Logger LOGGER = LoggerFactory.getLogger(GeneralConfigSynchronizer.class);
	
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) throws Exception {
		LOGGER.info(">>> 初始化{}...", getConfigDescription());
		updateConfig();
	}
	
	@Override
	public void onApplicationEvent(T event) {
		LOGGER.info("<<< 收到{}同步事件: {}", getConfigDescription(), event);
		updateConfig();
	}

	/**
	 * 获取配置的描述
	 * @return
	 */
	protected abstract String getConfigDescription();
	
	/**
	 * 更新配置
	 */
	protected abstract void updateConfig();
	
}
