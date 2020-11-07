package com.penglecode.xmodule.master4j.integrates.example.common.config;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.master4j.integrates.example.common.util.HikariDataSourceUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/10 22:34
 */
@Configuration
public class CommonDataSourceConfiguration extends AbstractSpringConfiguration {

    @Bean
    public DataSource defaultDataSource() {
        return HikariDataSourceUtils.createHikariDataSource(getEnvironment(), null);
    }

}
