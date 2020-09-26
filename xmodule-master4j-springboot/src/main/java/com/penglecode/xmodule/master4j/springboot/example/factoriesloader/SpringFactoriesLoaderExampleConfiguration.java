package com.penglecode.xmodule.master4j.springboot.example.factoriesloader;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;

import java.util.List;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/24 21:49
 */
@Configuration
@ConditionalOnProperty(prefix="spring.examples.factories-loader", name="enabled", havingValue="true")
public class SpringFactoriesLoaderExampleConfiguration extends AbstractSpringConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        List<String> autoConfigurationNames = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, ClassUtils.getDefaultClassLoader());
        System.out.println("【获取@EnableAutoConfiguration自动配置列表】>>> ");
        for(String autoConfigurationName : autoConfigurationNames) {
            System.out.println("\t" + autoConfigurationName);
        }
        System.out.println("【获取@EnableAutoConfiguration自动配置列表】<<< ");
    }

}
