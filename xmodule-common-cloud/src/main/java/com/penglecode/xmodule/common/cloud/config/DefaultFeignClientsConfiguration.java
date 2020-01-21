package com.penglecode.xmodule.common.cloud.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.DefaultHystrixFallbackFactory;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.cloud.openfeign.HystrixFallbackConfiguration;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.cloud.feign.DefaultHystrixFallbackHandlerFactory;
import com.penglecode.xmodule.common.cloud.feign.ObjectRequestParamToStringConverter;
import com.penglecode.xmodule.common.cloud.feign.StringToObjectRequestParamConverter;

import feign.Feign;
import feign.Logger;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

/**
 * 默认的Feign自定义配置
 * 
 * @see #FeignClientsConfiguration
 * @see #FeignAutoConfiguration
 * 
 * @author 	pengpeng
 * @date	2019年5月22日 下午5:45:06
 */
@Configuration
@ConditionalOnClass(Feign.class)
@Import({HystrixFallbackConfiguration.class})
public class DefaultFeignClientsConfiguration extends AbstractSpringConfiguration {

	@Autowired
	private ObjectFactory<HttpMessageConverters> messageConverters;
	
	/**
	 * feign-client日志配置
	 */
	@Bean
	public Logger.Level feignLoggerLevel(){
		return  Logger.Level.FULL;
	}
	
	/**
	 * 实现feign支持form表单提交
	 */
    @Bean
    public Encoder feignEncoder() {
    	return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }
	
    /**
     * 默认的全局fallback工厂配置
     */
	@Bean
	@Scope("prototype")
	@ConditionalOnMissingBean
	public DefaultHystrixFallbackFactory defaultHystrixFallbackFactory() {
		DefaultHystrixFallbackFactory defaultHystrixFallbackFactory = new DefaultHystrixFallbackFactory();
		defaultHystrixFallbackFactory.setFallbackHandlerFactory(new DefaultHystrixFallbackHandlerFactory());
		return defaultHystrixFallbackFactory;
	}
	
	/**
	 * 服务consumer端配置
	 */
	@Bean
	public List<FeignFormatterRegistrar> feignFormatterRegistrar() {
		return Arrays.asList(new DefaultFeignFormatterRegistrar());
	}
	
	/**
	 * 基于SpringMvc的服务provider端配置
	 */
	@Configuration
	@ConditionalOnWebApplication(type = Type.SERVLET)
	public static class DefaultFeignClientsWebMvcConfiguration implements WebMvcConfigurer {
		
		@Override
		public void addFormatters(FormatterRegistry registry) {
			registry.addConverter(new StringToObjectRequestParamConverter());
		}
		
	}
	
	/**
	 * 基于Webflux的服务provider端配置
	 */
	@Configuration
	@ConditionalOnWebApplication(type = Type.REACTIVE)
	public static class DefaultFeignClientsWebFluxConfiguration {
		
		//TODO
		
	}
	
	public static class DefaultFeignFormatterRegistrar implements FeignFormatterRegistrar {
		
		@Override
		public void registerFormatters(FormatterRegistry registry) {
			registry.addConverter(new ObjectRequestParamToStringConverter());
		}
		
	}
	
}
