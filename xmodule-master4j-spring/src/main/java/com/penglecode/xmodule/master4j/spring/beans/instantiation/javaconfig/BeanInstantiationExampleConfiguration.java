package com.penglecode.xmodule.master4j.spring.beans.instantiation.javaconfig;

import com.penglecode.xmodule.master4j.spring.beans.instantiation.BeanInstantiationBeanPostProcessor;
import com.penglecode.xmodule.master4j.spring.beans.instantiation.JettyWebServer;
import com.penglecode.xmodule.master4j.spring.beans.instantiation.ServerConfig;
import com.penglecode.xmodule.master4j.spring.beans.instantiation.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * Spring Bean实例化过程示例 (JavaConfig)
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/12 14:09
 */
@Configuration
public class BeanInstantiationExampleConfiguration {

    @Bean
    public ServerConfig serverConfig() {
        return new ServerConfig("127.0.0.1", 8080);
    }

    @Bean
    public TomcatWebServer tomcatWebServer() {
        return new TomcatWebServer(serverConfig());
    }

    @Bean
    public JettyWebServer jettyWebServer() {
        return new JettyWebServer(serverConfig());
    }

}
