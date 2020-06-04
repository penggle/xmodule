package com.penglecode.xmodule.common.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.initializer.DefaultWebServerPreStartupListener;

/**
 * 默认的Web应用(Servlet、Reactive)配置
 * 
 * @author 	pengpeng
 * @date 	2020年5月25日 上午10:48:14
 */
@Configuration
public class DefaultWebAppConfiguration extends AbstractSpringConfiguration {

	/**
	 * 默认的Web应用启动时的初始化程序主类
	 */
	@Bean
	public DefaultWebServerPreStartupListener defaultWebServerPreStartupListener() {
		return new DefaultWebServerPreStartupListener();
	}
	
}
