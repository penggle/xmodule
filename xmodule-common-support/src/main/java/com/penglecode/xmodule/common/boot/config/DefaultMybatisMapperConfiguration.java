package com.penglecode.xmodule.common.boot.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.support.DefaultDatabase;

/**
 * springboot-mybatis 集成配置
 * 
 * @author 	pengpeng
 * @date	2018年2月3日 下午1:55:15
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@ConditionalOnProperty(name="spring.mybatis.enabled", havingValue="true", matchIfMissing=false)
public class DefaultMybatisMapperConfiguration extends AbstractSpringConfiguration {

	@Bean(name="defaultMapperScannerConfigurer")
	public MapperScannerConfigurer defaultMapperScannerConfigurer() throws Exception {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setBasePackage(getEnvironment().getProperty("spring.mybatis.mapperScanPackage"));
		mapperScannerConfigurer.setAnnotationClass(DefaultDatabase.class);
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("defaultSqlSessionFactory");
		return mapperScannerConfigurer;
	}
	
}
