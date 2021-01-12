package com.penglecode.xmodule.common.boot.config;

import com.penglecode.xmodule.common.util.DataSourceUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

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
	 * 默认库的连接池配置
	 */
	@Bean(name="defaultDataSourceProperties")
	@ConfigurationProperties(prefix="spring.datasource.default")
	protected DataSourceProperties defaultDataSourceProperties() {
		return new DataSourceProperties();
	}

	/**
	 * 默认数据库的连接池配置
	 * 其中spring.datasource.hikari开头的配置可以当做一个公共的参数配置
	 */
	@Bean(name="defaultDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public DataSource defaultDataSource(@Qualifier("defaultDataSourceProperties") DataSourceProperties properties) {
		HikariDataSource dataSource = DataSourceUtils.createDataSource(properties, HikariDataSource.class);
		if (StringUtils.hasText(properties.getName())) {
			dataSource.setPoolName(properties.getName());
		}
		return dataSource;
	}
	
	@Bean(name="defaultJdbcTemplate")
	public JdbcTemplate defaultJdbcTemplate(@Qualifier("defaultDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
}