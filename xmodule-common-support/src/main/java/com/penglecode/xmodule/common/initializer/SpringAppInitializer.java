package com.penglecode.xmodule.common.initializer;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;

/**
 * 基于Spring事件的初始化操作
 * 如果想调整初始化的前后顺序，请在初始化实现类上重新标记@Order(..)
 * 
 * @author 	pengpeng
 * @date	2019年6月13日 上午11:26:47
 */
@Order(0)
public interface SpringAppInitializer {

	/**
	 * 执行初始化
	 * @param applicationContext
	 */
	public void initialize(ConfigurableApplicationContext applicationContext) throws Exception;
	
}
