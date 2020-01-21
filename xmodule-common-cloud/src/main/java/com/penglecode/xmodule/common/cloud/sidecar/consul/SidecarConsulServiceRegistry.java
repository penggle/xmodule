package com.penglecode.xmodule.common.cloud.sidecar.consul;

import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;

import com.ecwid.consul.v1.ConsulClient;

public class SidecarConsulServiceRegistry extends ConsulServiceRegistry {

	public SidecarConsulServiceRegistry(ConsulClient client, ConsulDiscoveryProperties properties,
			TtlScheduler ttlScheduler, HeartbeatProperties heartbeatProperties) {
		super(client, properties, ttlScheduler, heartbeatProperties);
		// TODO Auto-generated constructor stub
	}

}
