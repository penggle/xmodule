package com.penglecode.xmodule.common.boot.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import com.penglecode.xmodule.common.web.reactive.support.DefaultErrorWebExceptionHandler;

/**
 * 默认的WebFlux错误视图/响应配置，大部分copy自{@link #ErrorWebFluxAutoConfiguration}
 * 
 * @author 	pengpeng
 * @date	2019年6月5日 下午3:37:58
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnProperty(name=DefaultReactiveErrorWebFluxConfiguration.CONFIGURATION_ENABLED, havingValue="true", matchIfMissing=true)
public class DefaultReactiveErrorWebFluxConfiguration extends AbstractSpringConfiguration {

	public static final String CONFIGURATION_ENABLED = "server.error.customized.enabled";
	
	private final ServerProperties serverProperties;

	private final ApplicationContext applicationContext;

	private final ResourceProperties resourceProperties;

	private final List<ViewResolver> viewResolvers;

	private final ServerCodecConfigurer serverCodecConfigurer;

	public DefaultReactiveErrorWebFluxConfiguration(ServerProperties serverProperties,
			ResourceProperties resourceProperties,
			ObjectProvider<ViewResolver> viewResolversProvider,
			ServerCodecConfigurer serverCodecConfigurer,
			ApplicationContext applicationContext) {
		this.serverProperties = serverProperties;
		this.applicationContext = applicationContext;
		this.resourceProperties = resourceProperties;
		this.viewResolvers = viewResolversProvider.orderedStream()
				.collect(Collectors.toList());
		this.serverCodecConfigurer = serverCodecConfigurer;
	}

	@Bean
	@Order(-1)
	public ErrorWebExceptionHandler errorWebExceptionHandler(
			ErrorAttributes errorAttributes) {
		DefaultErrorWebExceptionHandler exceptionHandler = new DefaultErrorWebExceptionHandler(
				errorAttributes, this.resourceProperties,
				this.serverProperties.getError(), this.applicationContext);
		exceptionHandler.setViewResolvers(this.viewResolvers);
		exceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
		exceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
		return exceptionHandler;
	}
	
}
