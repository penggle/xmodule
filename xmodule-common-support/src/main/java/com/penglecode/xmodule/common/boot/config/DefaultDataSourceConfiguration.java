package com.penglecode.xmodule.common.boot.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * springboot-hikari多数据源配置
 * 
 * @author 	pengpeng
 * @date	2018年2月3日 下午1:54:23
 */
@Configuration
@ConditionalOnClass(HikariDataSource.class)
@ConditionalOnMissingBean(DataSource.class)
@ConditionalOnProperty(prefix="spring.datasource.default", name={"username", "password", "url"})
public class DefaultDataSourceConfiguration extends AbstractSpringConfiguration {

	/**
	 * 公共的连接池配置
	 */
	@Bean(name="commonHikariConfig")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	protected HikariConfig commonHikariConfig() {
		return new HikariConfig();
	}
	
	/**
	 * 默认库的连接池配置
	 */
	@Bean(name="defaultHikariConfig")
	@ConfigurationProperties(prefix = "spring.datasource.default")
	protected HikariConfig defaultHikariConfig(@Qualifier("commonHikariConfig") HikariConfig commonHikariConfig) {
		HikariConfig hikariConfig = new HikariConfig();
		commonHikariConfig.copyStateTo(hikariConfig);
		return hikariConfig;
	}
	
	/**
	 * 默认数据源
	 */
	@Bean(name="defaultDataSource", destroyMethod="close", initMethod="init")
	public DataSource defaultDataSource(@Qualifier("defaultHikariConfig") HikariConfig defaultHikariConfig) {
		return new HikariDataSource(defaultHikariConfig);
	}
	
	@Bean(name="defaultJdbcTemplate")
	public JdbcTemplate defaultJdbcTemplate(@Qualifier("defaultDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
}