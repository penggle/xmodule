package com.penglecode.xmodule.master4j.spring.aop.aspect;

import com.penglecode.xmodule.master4j.spring.aop.aspect.service.NewsService;
import com.penglecode.xmodule.master4j.spring.aop.aspect.service.RemoteService;
import com.penglecode.xmodule.master4j.spring.aop.aspect.service.WeatherService;
import com.penglecode.xmodule.master4j.spring.common.model.News;
import org.springframework.context.annotation.*;

/**
 * 基于@Aspect注解的Spring声明式AOP示例
 *
 * *@target* - 通过判断被代理类型是否标注了@target(annotation)中指定类型annotation的注解来匹配的切点，
 * 且annotation注解只能加在targetClass上，即实现类上才有效果，annotation注解加在接口上是没有效果的。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/20 14:55
 */
public class AspectJavaConfigBasedExample3 {

    @Configuration
    @EnableAspectJAutoProxy
    @ComponentScan(basePackageClasses=RemoteService.class)
    public static class ExampleConfiguration {

        @Bean
        public RemoteServiceLogAspect3 remoteServiceLogAspect3() {
            return new RemoteServiceLogAspect3();
        }

    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ExampleConfiguration.class);
        System.out.println(">>> applicationContext = " + applicationContext);
        System.out.println();

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
