package com.penglecode.xmodule.master4j.spring.context.config.property1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/17 17:05
 */
@Configuration
@PropertySource(name="applicationProperties", value={"classpath:application.properties"})
public class ExampleConfiguration {

    @Bean
    public AppConfig appConfig() {
        return new AppConfig();
    }

    /*@Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        //configurer.setLocation(new ClassPathResource("application.properties"));
        return configurer;
    }*/

}
