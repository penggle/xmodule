package com.penglecode.xmodule.common.boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.penglecode.xmodule.common.util.JsonUtils;

import io.lettuce.core.RedisClient;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;


/**
 * springboot与spring-data-redis集成
 * 
 * @author 	pengpeng
 * @date	2018年2月3日 下午2:56:41
 */
@Configuration
@ConditionalOnClass(RedisClient.class)
public class CommonRedisConfiguration extends AbstractSpringConfiguration {

	@Bean(name="commonRedisProperties")
	@ConfigurationProperties(prefix="spring.redis.common")
	public RedisProperties commonRedisProperties() {
		return new RedisProperties();
	}
	
	@Bean(name="defaultRedisKeySerializer")
	public RedisSerializer<String> defaultRedisKeySerializer() {
		return new StringRedisSerializer();
	}
	
	@SuppressWarnings("deprecation")
	@Bean(name="defaultRedisValueSerializer")
	public RedisSerializer<Object> defaultRedisValueSerializer() {
		ObjectMapper objectMapper = JsonUtils.createDefaultObjectMapper();
		objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
		return new GenericJackson2JsonRedisSerializer(objectMapper);
	}
	
	@Bean(name="defaultClientResources", destroyMethod="shutdown")
	public ClientResources defaultClientResources() {
		return DefaultClientResources.create();
	}
	
}
