package com.penglecode.xmodule.master4j.spring.common.datasource;

import com.zaxxer.hikari.HikariDataSource;
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
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));
        dataSource.setJdbcUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driverClassName", "com.mysql.cj.jdbc.Driver"));

        //#连接池名字
        dataSource.setPoolName(environment.getProperty("spring.datasource.hikari.poolName", "defaultHikariCP"));
        //最小空闲连接数量
        dataSource.setMinimumIdle(environment.getProperty("spring.datasource.hikari.minimumIdle", Integer.class, 5));
        //空闲连接存活最大时间，默认600000(10分钟)
        dataSource.setIdleTimeout(environment.getProperty("spring.datasource.hikari.idleTimeout", Integer.class, 180000));
        //连接池最大连接数，默认是10
        dataSource.setMaximumPoolSize(environment.getProperty("spring.datasource.hikari.maximumPoolSize(", Integer.class, 10));
        //池中连接的默认自动提交行为，默认值true
        dataSource.setAutoCommit(environment.getProperty("spring.datasource.hikari.autoCommit", Boolean.class, true));
        //池中连接的最长生命周期，0表示无限生命周期，默认1800000(30分钟)
        dataSource.setMaxLifetime(environment.getProperty("spring.datasource.hikari.maxLifetime", Integer.class, 1800000));
        //等待来自池的连接的最大毫秒数，默认30000(30秒)
        dataSource.setConnectionTimeout(environment.getProperty("spring.datasource.hikari.connectionTimeout", Integer.class, 30000));
        //连接测试语句
        dataSource.setConnectionTestQuery(environment.getProperty("spring.datasource.hikari.connectionTestQuery", "SELECT 1"));
        return dataSource;
    }

}
