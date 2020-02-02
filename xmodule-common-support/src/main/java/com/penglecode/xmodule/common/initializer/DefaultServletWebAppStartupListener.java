package com.penglecode.xmodule.common.initializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.penglecode.xmodule.common.exception.ApplicationInitializeException;
import com.penglecode.xmodule.common.util.CollectionUtils;

/**
 * 默认的在基于Servlet环境的Web应用启动时的初始化程序主类
 * 
 * @author 	pengpeng
 * @date 	2020年2月2日 下午12:57:49
 */
public class DefaultServletWebAppStartupListener implements ServletContextInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultServletWebAppStartupListener.class);
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		ConfigurableWebApplicationContext applicationContext = (ConfigurableWebApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		Map<String,SpringWebAppStartupInitializer> springWebAppInitializerBeans = applicationContext.getBeansOfType(SpringWebAppStartupInitializer.class);
		
		List<SpringWebAppStartupInitializer> initializers = new ArrayList<>(springWebAppInitializerBeans.values());
		if(!CollectionUtils.isEmpty(initializers)) {
			AnnotationAwareOrderComparator.sort(initializers);
			LOGGER.info(">>> 基于Servlet环境的Spring Web应用启动, 即将执行的初始化程序：{}", initializers);
			
			for(SpringWebAppStartupInitializer initializer : initializers) {
				try {
					initializer.initialize(applicationContext);
				} catch (Throwable e) {
					LOGGER.error(String.format(">>> 执行Spring Web应用启动初始化程序(%s)失败：%s", initializer.getClass().getSimpleName(), e.getMessage(), e));
					throw new ApplicationInitializeException(e);
				}
			}
		}
	}

}
