package com.penglecode.xmodule.master4j.spring.beans.injection;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;


/**
 * 依赖注入的综合示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/12 15:52
 */
public class DependencyInjectionExample {

    @Configuration
    @ComponentScan(basePackageClasses=DependencyInjectionExample.class)
    public static class ExampleConfiguration {

        /**
         * 工厂方法注入
         * @param restTemplate      - 等价于使用@Autowired自动注入
         * @param endpointsProvider - 等价于使用@Autowired按类型自动注入，只不过可以指定获取单个还是多个
         * @return
         */
        @Bean
        public UserApiService userApiService(RestTemplate restTemplate, ObjectProvider<List<ApiEndpoint>> endpointsProvider) {
            return new UserApiService(restTemplate, endpointsProvider.getIfAvailable());
        }

        @Bean
        public UserRepository userRepository() {
            return new UserRepository();
        }

        @Bean
        public ApiAccessLogger apiAccessLogger() {
            return new ApiAccessLogger();
        }

        @Bean
        public RestTemplate restTemplate() {
            OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();
            requestFactory.setConnectTimeout(6000);
            requestFactory.setReadTimeout(60000);
            requestFactory.setWriteTimeout(60000);
            return new RestTemplate(requestFactory);
        }

        @Bean
        public ApiEndpoint apiEndpoint1() {
            return new ApiEndpoint("192.168.137.101", 8080);
        }

        @Bean
        public ApiEndpoint apiEndpoint2() {
            return new ApiEndpoint("192.168.137.102", 8080);
        }

        @Bean
        public ApiEndpoint apiEndpoint3() {
            return new ApiEndpoint("192.168.137.103", 8080);
        }

    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ExampleConfiguration.class);
        System.out.println(applicationContext);
        applicationContext.registerShutdownHook(); //等价于applicationContext.close()，不过是被动的，仅在JVM关闭时触发
        //LockSupport.park();
        UserApiService userApiService = applicationContext.getBean(UserApiService.class);
        System.out.println(userApiService);

        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for(String beanName : beanNames) {
            BeanDefinition beanDefinition = applicationContext.getBeanDefinition(beanName);
            System.out.println(beanName + " ==> " + beanDefinition);
        }
    }

}
