package com.penglecode.xmodule.common.boot.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.support.DefaultDatabase;

/**
 * springboot-mybatis 集成配置
 * 
 * (注意：由于MapperScannerConfigurer的注册优先级很高，
 * 	所以该Configuration配置不要加@ConditionalOnBean(...)，
 * 	否则会出现XxxMapper bean找不到的问题)
 * 
 * @author 	pengpeng
 * @date	2018年2月3日 下午1:55:15
 */
@Configuration
@ConditionalOnClass(SqlSessionFactory.class)
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
