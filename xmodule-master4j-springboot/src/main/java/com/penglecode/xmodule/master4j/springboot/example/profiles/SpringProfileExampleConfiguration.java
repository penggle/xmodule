package com.penglecode.xmodule.master4j.springboot.example.profiles;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/23 18:05
 */
@Configuration
@ConditionalOnProperty(prefix="spring.examples.profiles", name="enabled", havingValue="true")
public class SpringProfileExampleConfiguration {

    @Bean
    @ConfigurationProperties(prefix="spring.examples.profiles.account")
    public AccountConfigProperties accountConfigProperties() {
        return new AccountConfigProperties();
    }

}
