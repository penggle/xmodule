package com.penglecode.xmodule.common.boot.config;

import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * springboot-druid多数据源配置
 * 
 * @author 	pengpeng
 * @date	2018年2月3日 下午1:54:23
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnMissingBean(DruidDataSource.class)
@ConditionalOnProperty(prefix="spring.datasource.default", name={"username", "password", "url"})
public class DefaultDataSourceConfiguration extends AbstractSpringConfiguration {

	/**
	 * 未指定具体数据库url,username,password的连接池配置
	 * @return
	 */
	@Bean(name="druidPoolConfig")
	@ConfigurationProperties(prefix = "spring.datasource.druid")
	protected Properties druidPoolConfig() {
		return new Properties();
	}
	
	@Bean(name="defaultDruidConfig")
	@ConfigurationProperties(prefix = "spring.datasource.default")
	protected Properties defaultDruidConfig() {
		return new Properties();
	}
	
	@Bean(name="defaultDataSource", destroyMethod="close", initMethod="init")
	public DataSource defaultDataSource(@Qualifier("druidPoolConfig") Properties druidPoolConfig, @Qualifier("defaultDruidConfig") Properties defaultDruidConfig) throws Exception {
		BeanWrapper druidDataSource = new BeanWrapperImpl(new DruidDataSource());
		Properties druidConfigProperties = new Properties();
		druidConfigProperties.putAll(druidPoolConfig);
		druidConfigProperties.putAll(defaultDruidConfig);
		for(Map.Entry<Object,Object> entry : druidConfigProperties.entrySet()) {
			String name = (String) entry.getKey();
			String value = (String) entry.getValue();
			druidDataSource.setPropertyValue(name, value);
		}
		DataSource dataSource = (DataSource) druidDataSource.getWrappedInstance();
		return dataSource;
	}
	
	@Bean(name="defaultJdbcTemplate")
	public JdbcTemplate defaultJdbcTemplate(@Qualifier("defaultDataSource") DataSource dataSource) throws Exception {
		return new JdbcTemplate(dataSource);
	}
	
}