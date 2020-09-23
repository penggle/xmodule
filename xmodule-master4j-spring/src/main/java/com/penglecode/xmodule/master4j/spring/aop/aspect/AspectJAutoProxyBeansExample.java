package com.penglecode.xmodule.master4j.spring.aop.aspect;

import com.penglecode.xmodule.master4j.spring.aop.aspect.service.NewsService;
import com.penglecode.xmodule.master4j.spring.aop.aspect.service.RemoteService;
import com.penglecode.xmodule.master4j.spring.aop.aspect.service.WeatherService;
import com.penglecode.xmodule.master4j.spring.common.model.News;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;

/**
 * 启用@EnableAspectJAutoProxy后，
 * 被自动代理的bean，在getBean()时都发生了什么?
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/20 22:27
 */
public class AspectJAutoProxyBeansExample {

    @Configuration
    @EnableAspectJAutoProxy
    @ComponentScan(basePackageClasses= RemoteService.class)
    public static class ExampleConfiguration {

        @Bean
        public RemoteServiceLogAspect1 remoteServiceLogAspect1() {
            return new RemoteServiceLogAspect1();
        }

    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AspectJAutoProxyBeansExample.ExampleConfiguration.class);
        System.out.println(">>> applicationContext = " + applicationContext);
        System.out.println();

        String[] beanNames = applicationContext.getBeanDefinitionNames();
        System.out.println("=============================================All BeanDefinitions(" + beanNames.length + ")=================================================");
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = applicationContext.getBeanFactory().getBeanDefinition(beanName);
            System.out.println(beanDefinition);
        }

        System.out.println("------------------------------------------------------------------------------");

        NewsService newsService = applicationContext.getBean(NewsService.class);
        newsService.getNewsListByPage(1);

        System.out.println();
        System.out.println();

        newsService.publishNews(new News());

        System.out.println();
        System.out.println();

        WeatherService weatherService = applicationContext.getBean(WeatherService.class);
        weatherService.getWeatherInfoByCity("101190101");

        System.out.println();
        System.out.println();
        weatherService.getWeatherInfoByCity(null);
    }

}
