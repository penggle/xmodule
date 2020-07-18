package com.penglecode.xmodule.common.boot.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.web.servlet.filter.AbstractHttpAccessLoggingFilter;
import com.penglecode.xmodule.common.web.support.HttpAccessLogDAO;

/**
 * 基于Servlet web应用访问日志记录自动配置
 * 
 * @author 	pengpeng
 * @date 	2020年1月17日 下午2:00:55
 */
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnBean({HttpAccessLogDAO.class, AbstractHttpAccessLoggingFilter.class})
public class DefaultServletHttpAccessLoggingConfiguration extends AbstractSpringConfiguration {
	
	/**
	 * 手动SpringMVC/Jersey等Http访问日志记录过滤器
	 */
	@Bean
	public FilterRegistrationBean<Filter> httpAccessLoggingFilterRegistration(AbstractHttpAccessLoggingFilter httpAccessLoggingFilter) {
	    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
	    filterRegistrationBean.setFilter(httpAccessLoggingFilter);
	    filterRegistrationBean.setEnabled(true);
	    filterRegistrationBean.setName("httpAccessLoggingFilter");
	    filterRegistrationBean.addUrlPatterns("/*");
	    filterRegistrationBean.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
	    filterRegistrationBean.setOrder(ServletFilterRegistrationOrder.ORDER_HTTP_ACCESS_LOGGING_FILTER);
	    return filterRegistrationBean;
	}
	
}
