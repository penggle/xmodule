package com.penglecode.xmodule.common.cloud.config;

import org.springframework.cloud.bus.ConditionalOnBusEnabled;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.BasePackage;
import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;

/**
 * 默认的SpringCloud-Bus配置
 * 
 * @author 	pengpeng
 * @date	2019年7月2日 上午10:14:17
 */
@Configuration
@ConditionalOnBusEnabled
@RemoteApplicationEventScan(basePackageClasses=BasePackage.class)
public class DefaultCloudBusConfiguration extends AbstractSpringConfiguration {

}
