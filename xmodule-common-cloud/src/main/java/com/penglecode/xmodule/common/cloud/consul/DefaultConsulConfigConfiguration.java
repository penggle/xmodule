package com.penglecode.xmodule.common.cloud.consul;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.consul.ConditionalOnConsulEnabled;
import org.springframework.cloud.consul.config.ConsulConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;

@Configuration
@ConditionalOnConsulEnabled
@ConditionalOnClass(ConsulConfigProperties.class)
@ConditionalOnProperty(name = "spring.cloud.consul.config.enabled", matchIfMissing = true)
public class DefaultConsulConfigConfiguration extends AbstractSpringConfiguration {

	/**
	 * 基于Consul的配置中心配置增删改查服务
	 * @return
	 */
	@Bean
	public ConsulConfigService consulConfigService() {
		return new ConsulConfigService();
	}
	
}
