package com.penglecode.xmodule.common.boot.config;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.support.GlobalAppConfig;
import com.penglecode.xmodule.common.util.ReflectionUtils;

/**
 * 默认的SpringBoot应用配置
 * 
 * @author 	pengpeng
 * @date	2019年8月28日 下午2:27:49
 */
@Configuration
public class DefaultSpringAppConfiguration extends AbstractSpringConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpringAppConfiguration.class);
	
	/**
	 * 全局应用配置
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix="global.app-config")
	public GlobalAppConfig globalAppConfig() {
		return GlobalConstants.GLOBAL_APP_CONFIG;
	}
	
	/**
	 * 全局默认的MessageSourceAccessor
	 */
	@Bean
	@ConditionalOnMissingBean(name="defaultMessageSourceAccessor")
	public MessageSourceAccessor defaultMessageSourceAccessor(MessageSource messageSource) {
		MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource, GlobalConstants.DEFAULT_LOCALE);
		LOGGER.info(">>> 初始化Spring应用的全局国际化资源文件配置! messageSource = {}, messageSourceAccessor = {}", messageSource, messageSourceAccessor);
		return messageSourceAccessor;
	}
	
	/**
	 * 全局默认的ResourcePatternResolver
	 */
	@Bean
	@ConditionalOnMissingBean(name="defaultResourcePatternResolver")
	public ResourcePatternResolver defaultResourcePatternResolver(AbstractApplicationContext applicationContext) {
		ResourcePatternResolver resourcePatternResolver = ReflectionUtils.getFieldValue(applicationContext, "resourcePatternResolver");
		LOGGER.info(">>> 初始化Spring应用的默认文件资源解析器配置! resourcePatternResolver = {}", resourcePatternResolver);
		return resourcePatternResolver;
	}
	
	/**
	 * 全局默认的ConversionService
	 */
	@Bean
	@ConditionalOnMissingBean(name="defaultConversionService")
	public ConversionService defaultConversionService() {
		ConversionService conversionService = ApplicationConversionService.getSharedInstance();
		DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        registrar.registerFormatters((FormatterRegistry) conversionService);
        LOGGER.info(">>> 初始化Spring应用的默认类型转换服务配置! conversionService = {}", conversionService.getClass());
        return conversionService;
	}
	
}
