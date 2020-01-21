package com.penglecode.xmodule.common.boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 默认的Restful请求客户端配置
 * 
 * @author 	pengpeng
 * @date 	2020年1月18日 下午5:44:12
 */
@Configuration
@ConditionalOnClass(WebClient.class)
public class DefaultReactiveWebClientConfiguration {

	/**
	 * 全局默认的WebClient
	 */
	@Bean
	@ConditionalOnBean(WebClient.Builder.class)
	@ConditionalOnMissingBean(name="defaultWebClient")
	public WebClient defaultWebClient(WebClient.Builder builder) {
		return builder.build();
	}
	
}
