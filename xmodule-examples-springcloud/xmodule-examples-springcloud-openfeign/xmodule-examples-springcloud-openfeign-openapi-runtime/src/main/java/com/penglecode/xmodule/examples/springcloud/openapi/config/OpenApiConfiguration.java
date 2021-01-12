package com.penglecode.xmodule.examples.springcloud.openapi.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/12/31 22:48
 */
@Configuration
public class OpenApiConfiguration {

    public OpenApiConfiguration(ObjectProvider<HttpMessageConverters> httpMessageConvertersProvider,
                                ObjectProvider<HandlerExceptionResolver> exceptionResolversProvider,
                                ObjectProvider<ConversionService> conversionServicesProvider) {
        HttpMessageConverters httpMessageConverters = httpMessageConvertersProvider.getIfAvailable();
        System.out.println("=================HttpMessageConverters=================");
        System.out.println(httpMessageConverters);
        System.out.println("=================HandlerExceptionResolver=================");
        exceptionResolversProvider.orderedStream().forEach(System.out::println);
        System.out.println("=================ConversionService=================");
        conversionServicesProvider.orderedStream().forEach(System.out::println);
    }

}
