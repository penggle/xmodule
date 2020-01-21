package com.penglecode.xmodule.common.boot.config;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.penglecode.xmodule.common.web.support.SilentlyResponseErrorHandler;

import okhttp3.OkHttpClient;

/**
 * 默认的Restful请求客户端配置
 * 
 * @author 	pengpeng
 * @date 	2020年1月18日 下午5:44:12
 */
@Configuration
@ConditionalOnClass({RestTemplate.class, OkHttpClient.class})
public class DefaultServletWebClientConfiguration {

	@Bean
	public RestTemplateBuilder restTemplateBuilder(ObjectProvider<HttpMessageConverters> messageConverters,
			ObjectProvider<RestTemplateCustomizer> restTemplateCustomizers,
			ObjectProvider<RestTemplateRequestCustomizer<?>> restTemplateRequestCustomizers) {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		HttpMessageConverters converters = messageConverters.getIfUnique();
		if (converters != null) {
			builder = builder.messageConverters(converters.getConverters());
		}
		builder = addCustomizers(builder, restTemplateCustomizers, RestTemplateBuilder::customizers);
		builder = addCustomizers(builder, restTemplateRequestCustomizers, RestTemplateBuilder::requestCustomizers);
		return builder;
	}
	
	/**
	 * 全局默认的RestTemplate
	 */
	@Bean
	@ConditionalOnBean(RestTemplateBuilder.class)
	@ConditionalOnMissingBean(name="defaultRestTemplate")
	public RestTemplate defaultRestTemplate(RestTemplateBuilder builder) {
		return builder.errorHandler(new SilentlyResponseErrorHandler()).requestFactory(() -> {
			OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();
			requestFactory.setConnectTimeout(6000);
			requestFactory.setReadTimeout(120000);
			requestFactory.setWriteTimeout(60000);
			return new BufferingClientHttpRequestFactory(requestFactory);
		}).build();
	}
	
	private <T> RestTemplateBuilder addCustomizers(RestTemplateBuilder builder, ObjectProvider<T> objectProvider,
			BiFunction<RestTemplateBuilder, Collection<T>, RestTemplateBuilder> method) {
		List<T> customizers = objectProvider.orderedStream().collect(Collectors.toList());
		if (!customizers.isEmpty()) {
			return method.apply(builder, customizers);
		}
		return builder;
	}
	
}
