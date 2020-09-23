package com.penglecode.xmodule.master4j.spring.context.sample.javaconfig2;

import com.penglecode.xmodule.master4j.spring.context.sample.common.JettyWebServer;
import com.penglecode.xmodule.master4j.spring.context.sample.common.TomcatWebServer;
import com.penglecode.xmodule.master4j.spring.context.sample.common.WebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 19:01
 */
@Configuration
@ComponentScan(basePackageClasses = { WebServer.class, AnnotationConfigApplicationContextExample2.class })
public class Example2Configuration {

    @Bean
    public WebServer jettyWebServer() {
        return new JettyWebServer(new InetSocketAddress("127.0.0.1", 8081));
    }

    @Bean
    public WebServer tomcatWebServer() {
        return new TomcatWebServer(new InetSocketAddress("127.0.0.1", 8082));
    }

}
