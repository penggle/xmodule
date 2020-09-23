package com.penglecode.xmodule.master4j.spring.context.sample.javaconfig1;

import com.penglecode.xmodule.master4j.spring.context.sample.common.JettyWebServer;
import com.penglecode.xmodule.master4j.spring.context.sample.common.TomcatWebServer;
import com.penglecode.xmodule.master4j.spring.context.sample.common.WebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * @Configuration是一个标准的ConfigurationClass类，
 * 其BeanDefinition#getAttribute(ConfigurationClassUtils.CONFIGURATION_CLASS_ATTRIBUTE) == ConfigurationClassUtils.CONFIGURATION_CLASS_FULL
 *
 * 其余具有ConfigurationClassUtils.candidateIndicators的或者具有@Bean工厂方法的bean，都是非标准ConfigurationClass类，
 * 其BeanDefinition#getAttribute(ConfigurationClassUtils.CONFIGURATION_CLASS_ATTRIBUTE) == ConfigurationClassUtils.CONFIGURATION_CLASS_LITE
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 16:02
 */
@Configuration
public class Example1FullConfiguration {

    @Bean
    public WebServer jettyWebServer() {
        return new JettyWebServer(new InetSocketAddress("127.0.0.1", 8081));
    }

    @Bean
    public WebServer tomcatWebServer() {
        return new TomcatWebServer(new InetSocketAddress("127.0.0.1", 8082));
    }

}
