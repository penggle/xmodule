package com.penglecode.xmodule.master4j.spring.context.sample.javaconfig1;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Configuration是一个标准的ConfigurationClass类，
 * 其BeanDefinition#getAttribute(ConfigurationClassUtils.CONFIGURATION_CLASS_ATTRIBUTE) == ConfigurationClassUtils.CONFIGURATION_CLASS_FULL
 *
 * 其余具有ConfigurationClassUtils.candidateIndicators的或者具有@Bean工厂方法的bean，都是非标准ConfigurationClass类，
 * 其BeanDefinition#getAttribute(ConfigurationClassUtils.CONFIGURATION_CLASS_ATTRIBUTE) == ConfigurationClassUtils.CONFIGURATION_CLASS_LITE
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 17:09
 */
@Component
//@Service //使用@Service也是可以的
public class Example1LiteConfiguration {

    public static class Foo {}

    public static class Bar {}

    @Bean
    public Foo foo() {
        return new Foo();
    }

    @Bean
    public Bar bar() {
        return new Bar();
    }

}
