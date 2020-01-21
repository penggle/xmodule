package com.penglecode.xmodule.common.cloud.sidecar.consul;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.context.ApplicationContextAware;

/**
 * 基于Consul的sidecar异构微服务自动注册
 * 
 * @author 	pengpeng
 * @date	2020年1月9日 下午1:56:42
 */
public class SidecarConsulAutoServiceRegistration extends ConsulAutoServiceRegistration implements ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(SidecarConsulAutoServiceRegistration.class);
	
	private final SidecarConsulAutoRegistration consulAutoRegistration;
	
	private final SidecarConsulDiscoveryProperties consulDiscoveryProperties;
	
	private final Map<ConsulDiscoveryProperties, SidecarConsulRegistrationsHolder> sidecarRegistrations;
	
	public SidecarConsulAutoServiceRegistration(ConsulServiceRegistry serviceRegistry,
			AutoServiceRegistrationProperties autoServiceRegistrationProperties, ConsulDiscoveryProperties properties,
			ConsulAutoRegistration registration) {
		super(serviceRegistry, autoServiceRegistrationProperties, properties, registration);
		this.consulAutoRegistration = (SidecarConsulAutoRegistration) registration;
		this.consulDiscoveryProperties = (SidecarConsulDiscoveryProperties) properties;
		this.sidecarRegistrations = consulAutoRegistration.createSidecarRegistrations(); //异构微服务本身的注册
	}

	@Override
	protected void register() {
		super.register(); //注册当前微服务
		
		LOGGER.info(">>> Preparing to register sidecar service: {}", consulDiscoveryProperties.getSidecar().getServiceName());
		sidecarRegistrations.forEach((properties, registrations) -> {
			if(properties.isRegister()) {
				try {
					registrations.getServiceRegistry().register(registrations.getRegistration()); //注册异构微服务
					LOGGER.info("<<< Succeed to register sidecar service instance: {}", consulDiscoveryProperties.getInstanceId());
				} catch (Throwable e) {
					LOGGER.error(">>> Failed to register sidecar service instance: {}", consulDiscoveryProperties.getInstanceId());
				}
			} else {
				LOGGER.warn(">>> Registration disabled for sidecar service instance: {}", consulDiscoveryProperties.getInstanceId());
			}
		});
	}

	@Override
	protected void deregister() {
		super.deregister(); //取消注册当前应用
		
		LOGGER.info(">>> Preparing to deregister sidecar service: {}", consulDiscoveryProperties.getSidecar().getServiceName());
		sidecarRegistrations.forEach((properties, registrations) -> {
			if (properties.isRegister() && properties.isDeregister()) {
				try {
					registrations.getServiceRegistry().deregister(registrations.getRegistration()); //取消注册异构微服务
					LOGGER.info("<<< Succeed to deregister sidecar service instance: {}", consulDiscoveryProperties.getInstanceId());
				} catch (Throwable e) {
					LOGGER.error(">>> Failed to deregister sidecar service instance: {}", consulDiscoveryProperties.getInstanceId());
				}
			}
		});
	}

	protected SidecarConsulAutoRegistration getConsulAutoRegistration() {
		return consulAutoRegistration;
	}

	protected SidecarConsulDiscoveryProperties getConsulDiscoveryProperties() {
		return consulDiscoveryProperties;
	}

	protected Map<ConsulDiscoveryProperties, SidecarConsulRegistrationsHolder> getSidecarRegistrations() {
		return sidecarRegistrations;
	}
	
}
