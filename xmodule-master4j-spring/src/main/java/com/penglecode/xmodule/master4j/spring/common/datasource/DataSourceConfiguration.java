package com.penglecode.xmodule.master4j.spring.common.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.sql.DataSource;

/**
 * 公共的数据源配置
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/21 13:18
 */
@Configuration
@PropertySource("classpath:jdbc.properties")
public class DataSourceConfiguration {

    @Bean
    public DataSource defaultDataSource(ConfigurableEnvironment environment) {
        return HikariDataSourceUtils.createHikariDataSource(environment, null);
    }

}
