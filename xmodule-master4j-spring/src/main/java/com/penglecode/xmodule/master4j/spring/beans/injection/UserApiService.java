package com.penglecode.xmodule.master4j.spring.beans.injection;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/12 15:27
 */
public class UserApiService implements InitializingBean, DisposableBean {

    private final RestTemplate restTemplate;

    private final List<ApiEndpoint> apiEndpoints;

    @Autowired
    private ApiAccessLogger apiAccessLogger;

    private UserRepository userRepository;

    public UserApiService(RestTemplate restTemplate, List<ApiEndpoint> apiEndpoints) {
        this.restTemplate = restTemplate;
        this.apiEndpoints = apiEndpoints;
    }

    public User getUserById(Long userId) {
        apiAccessLogger.log("UserApiService#getUserById(" + userId + ")");
        return new User();
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("UserApiService#destroy()");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("UserApiService#afterPropertiesSet()");
    }

}
