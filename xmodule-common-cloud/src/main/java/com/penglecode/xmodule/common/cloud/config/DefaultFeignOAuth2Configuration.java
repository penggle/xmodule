package com.penglecode.xmodule.common.cloud.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.cloud.feign.oauth2.ReactiveCloudOAuth2AuthApplyFeignInterceptor;
import com.penglecode.xmodule.common.cloud.feign.oauth2.ServletCloudOAuth2AuthApplyFeignInterceptor;

import feign.Feign;
import feign.RequestInterceptor;

/**
 * SpringCloud Feign API接口调用鉴权OAuth2配置
 * 
 * @author 	pengpeng
 * @date 	2020年2月12日 下午2:56:43
 */
@Configuration
@ConditionalOnClass({Feign.class, OAuth2AuthorizedClient.class})
public class DefaultFeignOAuth2Configuration extends AbstractSpringConfiguration {

	@Configuration
	@ConditionalOnWebApplication(type=Type.SERVLET)
	public static class ServletCloudFeignOAuth2Configuration {
		
		/**
		 * 微服务之间API接口调用鉴权OAuth2认证拦截器
		 */
		@Bean
		public RequestInterceptor cloudOAuth2AuthApplyFeignInterceptor() {
			return new ServletCloudOAuth2AuthApplyFeignInterceptor();
		}
		
	}
	
	@Configuration
	@ConditionalOnWebApplication(type=Type.REACTIVE)
	public static class ReactiveCloudFeignOAuth2Configuration {
		
		/**
		 * 微服务之间API接口调用鉴权OAuth2认证拦截器
		 */
		@Bean
		public RequestInterceptor cloudOAuth2AuthApplyFeignInterceptor() {
			return new ReactiveCloudOAuth2AuthApplyFeignInterceptor();
		}
		
	}
	
}
