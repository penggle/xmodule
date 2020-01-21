package com.penglecode.xmodule.common.boot.config;

import java.util.List;

import javax.servlet.Servlet;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import com.penglecode.xmodule.common.web.servlet.support.DefaultErrorController;

@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@ConditionalOnProperty(name=DefaultServletErrorMvcConfiguration.CONFIGURATION_ENABLED, havingValue="true", matchIfMissing=true)
public class DefaultServletErrorMvcConfiguration extends AbstractSpringConfiguration {

	public static final String CONFIGURATION_ENABLED = "server.error.customized.enabled";
	
	private final ServerProperties serverProperties;

	private final List<ErrorViewResolver> errorViewResolvers;

	public DefaultServletErrorMvcConfiguration(ServerProperties serverProperties, ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {
		super();
		this.serverProperties = serverProperties;
		this.errorViewResolvers = errorViewResolversProvider.getIfAvailable();
	}
	
	@Bean
	public DefaultErrorController defaultErrorController(ErrorAttributes errorAttributes) {
		return new DefaultErrorController(errorAttributes, this.serverProperties.getError(),
				this.errorViewResolvers);
	}
	
	@Bean
	public ErrorAttributes defaultErrorAttributes() {
		return new DefaultErrorAttributes();
	}
	
}
