package com.penglecode.xmodule.common.cloud.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.cloud.gateway.filter.factory.CustomRetryGatewayFilterFactory;

/**
 * 默认的springcloud-gateway配置
 * 
 * @author 	pengpeng
 * @date 	2020年1月19日 下午4:20:43
 */
@Configuration
@ConditionalOnWebApplication(type = Type.REACTIVE)
public class DefaultGatewayConfiguration extends AbstractSpringConfiguration {

	@Bean
	public CustomRetryGatewayFilterFactory customRetryGatewayFilterFactory() {
		return new CustomRetryGatewayFilterFactory();
	}
	
}
