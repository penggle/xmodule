package com.penglecode.xmodule.common.cloud.sidecar.config;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.consul.ConditionalOnConsulEnabled;
import org.springframework.cloud.consul.discovery.ConditionalOnConsulDiscoveryEnabled;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulManagementRegistrationCustomizer;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistrationCustomizer;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penglecode.xmodule.common.cloud.sidecar.consul.SidecarConsulAutoRegistration;
import com.penglecode.xmodule.common.cloud.sidecar.consul.SidecarConsulAutoServiceRegistration;
import com.penglecode.xmodule.common.cloud.sidecar.consul.SidecarConsulDiscoveryProperties;

/**
 * 基于Consul的sidecar异构应用的配置
 * 
 * @author 	pengpeng
 * @date	2020年1月9日 下午1:44:18
 */
@Configuration
@ConditionalOnConsulEnabled
@ConditionalOnConsulDiscoveryEnabled
@ConditionalOnProperty(prefix="spring.cloud.consul.discovery.sidecar", name={"service-name"})
public class ConsulSidecarApplicationConfiguration {

	@Bean
	@ConfigurationProperties(prefix="spring.cloud.consul.discovery")
	public SidecarConsulDiscoveryProperties consulDiscoveryProperties(InetUtils inetUtils) {
		return new SidecarConsulDiscoveryProperties(inetUtils);
	}
	
	/**
	 * 基于Consul的服务注册信息
	 */
	@Bean
	public ConsulAutoRegistration consulRegistration(
			AutoServiceRegistrationProperties autoServiceRegistrationProperties,
			ConsulDiscoveryProperties properties, ApplicationContext applicationContext,
			ObjectProvider<List<ConsulRegistrationCustomizer>> registrationCustomizers,
			ObjectProvider<List<ConsulManagementRegistrationCustomizer>> managementRegistrationCustomizers,
			HeartbeatProperties heartbeatProperties) {
		return SidecarConsulAutoRegistration.sidecarRegistration(autoServiceRegistrationProperties,
				properties, applicationContext, registrationCustomizers.getIfAvailable(),
				managementRegistrationCustomizers.getIfAvailable(), heartbeatProperties);
	}
	
	/**
	 * 基于Consul的服务注册者
	 */
	@Bean
	public ConsulAutoServiceRegistration consulAutoServiceRegistration(
			ConsulServiceRegistry registry,
			AutoServiceRegistrationProperties autoServiceRegistrationProperties,
			ConsulDiscoveryProperties properties,
			ConsulAutoRegistration consulRegistration) {
		return new SidecarConsulAutoServiceRegistration(registry,
				autoServiceRegistrationProperties, properties, consulRegistration);
	}
	
}
