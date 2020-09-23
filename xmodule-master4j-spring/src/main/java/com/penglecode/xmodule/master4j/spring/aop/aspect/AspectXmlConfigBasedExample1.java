package com.penglecode.xmodule.master4j.spring.aop.aspect;

import com.penglecode.xmodule.master4j.spring.aop.aspect.service.NewsService;
import com.penglecode.xmodule.master4j.spring.aop.aspect.service.WeatherService;
import com.penglecode.xmodule.master4j.spring.common.model.News;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 基于@Aspect注解的Spring声明式AOP示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/20 14:55
 */
public class AspectXmlConfigBasedExample1 {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {"applicationContext1.xml"}, AspectXmlConfigBasedExample1.class);
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
