package com.penglecode.xmodule.common.web.servlet.support;

import java.beans.Introspector;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.context.ConfigurableWebServerApplicationContext;
import org.springframework.boot.web.servlet.AbstractFilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializerBeans;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.ReflectionUtils;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * 打印SpringBoot自动注册的Filter的顺序
 * 
 * @author pengpeng
 * @date 2018年6月5日 上午9:43:07
 */
public class EnabledFilterRegistrationBeanLogger implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnabledFilterRegistrationBeanLogger.class);
	
	@Override
	@SuppressWarnings({ "unchecked" })
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext() instanceof ConfigurableWebServerApplicationContext) {
			ConfigurableWebServerApplicationContext applicationContext = (ConfigurableWebServerApplicationContext) event.getApplicationContext();
			ServletContextInitializerBeans servletContextInitializerBeans = new ServletContextInitializerBeans(applicationContext.getBeanFactory());
			servletContextInitializerBeans.forEach(sci -> {
				if(sci instanceof AbstractFilterRegistrationBean) {
					AbstractFilterRegistrationBean<Filter> filterRegBean = (AbstractFilterRegistrationBean<Filter>) sci;
					if(filterRegBean.isEnabled()) {
						String filterName = ReflectionUtils.getFieldValue(ReflectionUtils.findField(filterRegBean.getClass(), "name"), filterRegBean);
						if(StringUtils.isEmpty(filterName)) {
							Filter filter = filterRegBean.getFilter();
							if(filter != null) {
								filterName = Introspector.decapitalize(filter.getClass().getSimpleName());
							} else {
								filterName = filterRegBean.toString();
							}
						}
						Collection<String> filterUrls = CollectionUtils.defaultIfEmpty(filterRegBean.getUrlPatterns(), Arrays.asList("/*"));
						LOGGER.info(">>> Register filter: {} urls={}, with order: {}", filterName, filterUrls, filterRegBean.getOrder());
					}
				}
			});
		}
	}

}
