package com.penglecode.xmodule.master4j.integrates.example.common.config;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executor;

/**
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/26 16:57
 */
@EnableAsync
@EnableScheduling
@Configuration
public class CommonTaskSchedulingConfiguration extends AbstractSpringConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        String[] executorNames = getApplicationContext().getBeanNamesForType(Executor.class);
        ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) getApplicationContext().getAutowireCapableBeanFactory();
        System.out.println("--------------------All Executor BeanDefinition-----------------");
        for(String executorName : executorNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(executorName);
            System.out.println(beanDefinition);
        }
    }

}
