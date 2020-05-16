package com.penglecode.xmodule.common.boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

import com.penglecode.xmodule.common.web.servlet.support.EnabledFilterRegistrationBeanLogger;
import com.penglecode.xmodule.common.web.support.DefaultSmartFileServerService;
import com.penglecode.xmodule.common.web.support.SmartFileServerService;

/**
 * 默认的WEB应用配置
 * 
 * @author 	pengpeng
 * @date	2018年2月3日 下午5:53:07
 */
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
public class DefaultServletWebAppConfiguration extends AbstractSpringConfiguration {

	/**
	 * 小型文件服务器的文件服务
	 */
	@Bean
	public SmartFileServerService smartFileServerService() {
		return new DefaultSmartFileServerService();
	}
	
	/**
	 * 配置RequestContextListener
	 */
	@Bean 
	public RequestContextListener requestContextListener(){
	    return new RequestContextListener();
	}
	
	/**
	 * Filter注册日志打印
	 */
	@Bean
	public EnabledFilterRegistrationBeanLogger enabledFilterRegistrationBeanLogger() {
		return new EnabledFilterRegistrationBeanLogger();
	}
	
}
