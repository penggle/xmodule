package com.penglecode.xmodule.master4j.springboot.example.conditions;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Spring @Conditional注解示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/24 16:36
 */
@Configuration
@ConditionalOnProperty(prefix="spring.examples.conditions", name="enabled", havingValue="true")
public class SpringConditionalExampleConfiguration {

    @Bean
    @Conditional(WindowsCondition.class)
    public WindowsOperatingSystem windowsOperatingSystem() {
        return new WindowsOperatingSystem();
    }

}
