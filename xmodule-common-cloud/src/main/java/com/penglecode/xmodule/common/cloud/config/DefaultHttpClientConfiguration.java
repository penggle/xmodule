package com.penglecode.xmodule.common.cloud.config;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;

import okhttp3.OkHttpClient;

/**
 * 默认的SpringCloud HTTP客户端配置
 * 
 * @author 	pengpeng
 * @date	2020年1月8日 下午3:47:21
 */
@Configuration
@ConditionalOnClass(OkHttpClient.class)
public class DefaultHttpClientConfiguration extends AbstractSpringConfiguration {

	@Bean
	public OkHttpClient.Builder okHttpClientBuilder() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(getEnvironment().getProperty("feign.okhttp.connection-timeout", Long.class, 15000L), TimeUnit.MILLISECONDS);
		builder.readTimeout(getEnvironment().getProperty("feign.okhttp.read-timeout", Long.class, 120000L), TimeUnit.MILLISECONDS);
		builder.writeTimeout(getEnvironment().getProperty("feign.okhttp.write-timeout", Long.class, 60000L), TimeUnit.MILLISECONDS);
		return builder;
	}
	
}
