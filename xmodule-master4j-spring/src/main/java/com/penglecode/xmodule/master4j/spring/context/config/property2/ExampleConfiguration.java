package com.penglecode.xmodule.master4j.spring.context.config.property2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/17 20:19
 */
@Configuration
public class ExampleConfiguration {

    @Configuration
    @Profile("dev")
    @PropertySource(name="applicationProperties", value={"classpath:com/penglecode/xmodule/master4j/spring/context/config/property2/application-dev.properties"})
    public static class DevConfiguration {

    }

    @Configuration
    @Profile("prd")
    @PropertySource(name="applicationProperties", value={"classpath:com/penglecode/xmodule/master4j/spring/context/config/property2/application-prd.properties"})
    public static class PrdConfiguration {

    }

    @Bean
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

}
