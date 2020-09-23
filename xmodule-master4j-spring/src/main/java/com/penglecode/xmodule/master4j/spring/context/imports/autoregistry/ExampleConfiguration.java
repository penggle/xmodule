package com.penglecode.xmodule.master4j.spring.context.imports.autoregistry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动注册@Mapper注释的类示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 23:42
 */
@Configuration
@MapperScan(basePackages = "com.penglecode.xmodule.master4j.spring.context")
public class ExampleConfiguration {

    @Bean
    public MyBeanDefinitionRegistryPostProcessor myBeanDefinitionRegistryPostProcessor() {
        return new MyBeanDefinitionRegistryPostProcessor();
    }

}
