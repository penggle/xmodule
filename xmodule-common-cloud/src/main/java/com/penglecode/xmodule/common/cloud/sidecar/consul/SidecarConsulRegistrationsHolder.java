package com.penglecode.xmodule.common.cloud.sidecar.consul;

import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;

public class SidecarConsulRegistrationsHolder {

	private final ConsulAutoRegistration registration;
	
	private final ConsulServiceRegistry serviceRegistry;

	public SidecarConsulRegistrationsHolder(ConsulAutoRegistration registration, ConsulServiceRegistry serviceRegistry) {
		super();
		this.registration = registration;
		this.serviceRegistry = serviceRegistry;
	}

	public ConsulAutoRegistration getRegistration() {
		return registration;
	}

	public ConsulServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}
	
}
