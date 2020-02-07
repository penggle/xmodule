package com.penglecode.xmodule.common.boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import com.penglecode.xmodule.common.web.webflux.support.ResultEntityResponseReativeConfiguration;

/**
 * Webflux配置
 * 
 * @author 	pengpeng
 * @date 	2020年2月7日 下午7:14:09
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass(WebFluxConfigurer.class)
@ConditionalOnProperty(name=DefaultReactiveWebFluxConfiguration.CONFIGURATION_ENABLED, havingValue="true", matchIfMissing=true)
public class DefaultReactiveWebFluxConfiguration extends AbstractSpringConfiguration implements WebFluxConfigurer {

	public static final String CONFIGURATION_ENABLED = "spring.webflux.customized.enabled";

	@Override
	public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
		//add custom HttpMessageCodes here
	}
	
	@Bean
	public ResultEntityResponseReativeConfiguration resultEntityResponseReativeConfiguration() {
		return new ResultEntityResponseReativeConfiguration();
	}
	
}
