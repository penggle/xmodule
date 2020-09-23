package com.penglecode.xmodule.master4j.spring.context.sample.javaconfig2;

import com.penglecode.xmodule.master4j.spring.context.sample.javaconfig1.Example1LiteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 19:04
 */
@Configuration
public class AdditionalConfiguration {

    public static class Foo {}

    public static class Bar {}

    @Bean
    public Example1LiteConfiguration.Foo foo() {
        return new Example1LiteConfiguration.Foo();
    }

    @Bean
    public Example1LiteConfiguration.Bar bar() {
        return new Example1LiteConfiguration.Bar();
    }

}
