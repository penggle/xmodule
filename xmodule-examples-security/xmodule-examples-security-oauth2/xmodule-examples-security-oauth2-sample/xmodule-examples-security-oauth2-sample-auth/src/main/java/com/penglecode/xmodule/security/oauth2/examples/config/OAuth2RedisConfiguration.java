package com.penglecode.xmodule.security.oauth2.examples.config;

import static com.penglecode.xmodule.common.redis.LettuceConnectionFactoryUtils.createLettuceClientConfiguration;
import static com.penglecode.xmodule.common.redis.LettuceConnectionFactoryUtils.createLettuceConnectionFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.util.BeanUtils;

import io.lettuce.core.resource.ClientResources;

/**
 * OAuth2的Redis数据库配置
 * 
 * @author pengpeng
 * @date 2019年12月26日 下午3:00:34
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.redis.default", name = "host", matchIfMissing = false)
public class OAuth2RedisConfiguration extends AbstractSpringConfiguration {

	@Bean(name = "defaultRedisProperties")
	@ConfigurationProperties(prefix = "spring.redis.default")
	public RedisProperties defaultRedisProperties(
			@Qualifier("commonRedisProperties") RedisProperties commonRedisProperties) {
		return BeanUtils.deepClone(commonRedisProperties);
	}

	@Bean(name = "defaultClientConfiguration")
	public LettuceClientConfiguration defaultClientConfiguration(
			@Qualifier("defaultRedisProperties") RedisProperties properties,
			@Qualifier("defaultClientResources") ClientResources clientResources) {
		return createLettuceClientConfiguration(properties, clientResources, properties.getLettuce().getPool());
	}

	@Bean(name = "defaultRedisConnectionFactory")
	public RedisConnectionFactory defaultRedisConnectionFactory(
			@Qualifier("defaultRedisProperties") RedisProperties properties,
			@Qualifier("defaultClientConfiguration") LettuceClientConfiguration clientConfiguration) {
		return createLettuceConnectionFactory(properties, clientConfiguration);
	}
	
}