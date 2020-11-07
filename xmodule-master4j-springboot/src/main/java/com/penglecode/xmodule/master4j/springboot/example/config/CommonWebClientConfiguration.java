package com.penglecode.xmodule.master4j.springboot.example.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 公共的Web请求客户端(RestTemplate|WebClient)配置
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/3 13:32
 */
@Configuration
public class CommonWebClientConfiguration {

    @Bean
    public RestTemplate defaultRestTemplate(RestTemplateBuilder builder) {
        return builder.requestFactory(this::clientHttpRequestFactory)
                      .build();
    }

    protected ClientHttpRequestFactory clientHttpRequestFactory() {
        OkHttp3ClientHttpRequestFactory clientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(6000);
        clientHttpRequestFactory.setReadTimeout(60000);
        clientHttpRequestFactory.setWriteTimeout(60000);
        return clientHttpRequestFactory;
    }

}
