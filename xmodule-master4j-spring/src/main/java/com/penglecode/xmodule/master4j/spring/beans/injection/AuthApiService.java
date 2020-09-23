package com.penglecode.xmodule.master4j.spring.beans.injection;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/12 15:27
 */
@Component
public class AuthApiService implements InitializingBean, DisposableBean {

    private final RestTemplate restTemplate;

    private final List<ApiEndpoint> apiEndpoints;

    @Autowired
    private ApiAccessLogger apiAccessLogger;

    /*public AuthApiService(RestTemplate restTemplate) {
        this(restTemplate, Collections.emptyList());
    }*/

    /**
     * 自动按类型注入，默认是隐式的@Autowired，当某个类型的实例在容器中存在多个时，处理办法：
     * 1、加@Qualifier("your bean name")指定bean的名称
     * 2、直接使用@Resource(name="your bean name")取代默认的@Autowired
     * 3、给同类型的多个实例的某个加上@Primary注解
     */
    public AuthApiService(RestTemplate restTemplate, List<ApiEndpoint> apiEndpoints) {
        this.restTemplate = restTemplate;
        this.apiEndpoints = apiEndpoints;
    }

    //等同于上面
    /*public AuthApiService(RestTemplate restTemplate, ObjectProvider<List<ApiEndpoint>> apiEndpointsProvider) {
        this.restTemplate = restTemplate;
        this.apiEndpoints = apiEndpointsProvider.getIfAvailable();
    }*/

    public String login(User user) {
        apiAccessLogger.log("AuthApiService#login(" + user + ")");
        return UUID.randomUUID().toString();
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("AuthApiService#destroy()");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("AuthApiService#afterPropertiesSet()");
        //System.out.println("apiEndpoints = " + apiEndpoints);
    }

}
