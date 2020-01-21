package com.penglecode.xmodule.common.cloud.sidecar.consul;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.discovery.SidecarDelegateHealthChecker;
import org.springframework.cloud.consul.discovery.SidecarDelegateHeartbeatTask;
import org.springframework.cloud.consul.discovery.SidecarTtlScheduler;
import org.springframework.cloud.consul.discovery.TtlScheduler;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulManagementRegistrationCustomizer;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistrationCustomizer;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import com.penglecode.xmodule.common.util.URIUtils;

/**
 * 基于Consul的sidecar异构微服务自动注册
 * 
 * @author 	pengpeng
 * @date	2020年1月9日 上午10:20:59
 */
public class SidecarConsulAutoRegistration extends ConsulAutoRegistration {

	private final AutoServiceRegistrationProperties autoServiceRegistrationProperties;

	private final SidecarConsulDiscoveryProperties consulDiscoveryProperties;
	
	private final ApplicationContext applicationContext;

	private final HeartbeatProperties heartbeatProperties;

	private final List<ConsulManagementRegistrationCustomizer> managementRegistrationCustomizers;
	
	private final List<ConsulRegistrationCustomizer> registrationCustomizers;
	
	private final HeartbeatProperties delegateHeartbeatProperties;
	
	public SidecarConsulAutoRegistration(NewService service,
			AutoServiceRegistrationProperties autoServiceRegistrationProperties, SidecarConsulDiscoveryProperties consulDiscoveryProperties,
			ApplicationContext applicationContext, HeartbeatProperties heartbeatProperties,
			List<ConsulManagementRegistrationCustomizer> managementRegistrationCustomizers,
			List<ConsulRegistrationCustomizer> registrationCustomizers) {
		super(service, autoServiceRegistrationProperties, consulDiscoveryProperties, applicationContext, heartbeatProperties,
				managementRegistrationCustomizers);
		this.autoServiceRegistrationProperties = autoServiceRegistrationProperties;
		this.consulDiscoveryProperties = consulDiscoveryProperties;
		this.applicationContext = applicationContext;
		this.heartbeatProperties = heartbeatProperties;
		this.managementRegistrationCustomizers = managementRegistrationCustomizers;
		this.registrationCustomizers = registrationCustomizers;
		this.delegateHeartbeatProperties = createDelegateHeartbeatProperties();
	}
	
	public static ConsulAutoRegistration sidecarRegistration(
			AutoServiceRegistrationProperties autoServiceRegistrationProperties,
			ConsulDiscoveryProperties properties, ApplicationContext context,
			List<ConsulRegistrationCustomizer> registrationCustomizers,
			List<ConsulManagementRegistrationCustomizer> managementRegistrationCustomizers,
			HeartbeatProperties heartbeatProperties) {

		NewService service = new NewService();
		String appName = getAppName(properties, context.getEnvironment());
		service.setId(getInstanceId(properties, context));
		if (!properties.isPreferAgentAddress()) {
			service.setAddress(properties.getHostname());
		}
		service.setName(normalizeForDns(appName));
		service.setTags(createTags(properties));

		if (properties.getPort() != null) {
			service.setPort(properties.getPort());
			// we know the port and can set the check
			setCheck(service, autoServiceRegistrationProperties, properties, context,
					heartbeatProperties);
		}

		ConsulAutoRegistration registration = new SidecarConsulAutoRegistration(service,
				autoServiceRegistrationProperties, (SidecarConsulDiscoveryProperties) properties, context,
				heartbeatProperties, managementRegistrationCustomizers, registrationCustomizers);
		customize(registrationCustomizers, registration);
		return registration;
	}
	
	/**
	 * 创建异构微服务的各个实例的ConsulAutoRegistration
	 * @return
	 */
	public Map<ConsulDiscoveryProperties,SidecarConsulRegistrationsHolder> createSidecarRegistrations() {
		ConsulClient client = applicationContext.getBean(ConsulClient.class);
		Map<ConsulDiscoveryProperties,SidecarConsulRegistrationsHolder> registrations = new HashMap<>();
		List<ConsulDiscoveryProperties> sidecarConsulDiscoveryConfigs = consulDiscoveryProperties.getSidecarConsulDiscoveryConfigs();
		Assert.notEmpty(sidecarConsulDiscoveryConfigs, "No sidecar service provider configuration found! Please check property: spring.cloud.consul.discovery.sidecar.*");
		for(ConsulDiscoveryProperties sidecarProperties : sidecarConsulDiscoveryConfigs) {
			TtlScheduler sidecarTtlScheduler = createSidecarTtlScheduler(sidecarProperties, client);
			ConsulAutoRegistration registration = registration(autoServiceRegistrationProperties, sidecarProperties, applicationContext, registrationCustomizers, managementRegistrationCustomizers, delegateHeartbeatProperties);
			ConsulServiceRegistry serviceRegistry = new ConsulServiceRegistry(client, sidecarProperties, sidecarTtlScheduler, delegateHeartbeatProperties);
			registrations.put(sidecarProperties, new SidecarConsulRegistrationsHolder(registration, serviceRegistry));
		}
		return registrations;
    }
	
	protected TtlScheduler createSidecarTtlScheduler(ConsulDiscoveryProperties consulDiscoveryProperties, ConsulClient client) {
		if(delegateHeartbeatProperties.isEnabled()) {
			//如果是启用sidecar服务代理异构微服务进行TTL健康检测的话，那么必须提供一个SidecarDelegateHeartbeatTask的实现并注册到Spring上下文中去
			SidecarDelegateHealthChecker delegateHealthChecker = applicationContext.getBean(SidecarDelegateHealthChecker.class);
			String url = String.format("%s://%s:%s", consulDiscoveryProperties.getScheme(), consulDiscoveryProperties.getHostname(), consulDiscoveryProperties.getPort());
			URL instanceUrl = URIUtils.createURL(url);
			SidecarDelegateHeartbeatTask delegateHeartbeatTask = new SidecarDelegateHeartbeatTask(client, consulDiscoveryProperties.getInstanceId(), instanceUrl, delegateHealthChecker);
			return new SidecarTtlScheduler(delegateHeartbeatProperties, client, delegateHeartbeatTask);
		}
		return null;
	}
	
	protected HeartbeatProperties createDelegateHeartbeatProperties() {
		HeartbeatProperties heartbeatProperties = new HeartbeatProperties();
		if(consulDiscoveryProperties.getSidecar().isDelegateHeartbeatEnabled()) {
			heartbeatProperties.setEnabled(true); //启用TTL健康检测
			heartbeatProperties.setTtlValue((int)consulDiscoveryProperties.getSidecar().getParsedDelegateHeartbeatInterval().getSeconds());
		}
		return heartbeatProperties;
	}

	protected AutoServiceRegistrationProperties getAutoServiceRegistrationProperties() {
		return autoServiceRegistrationProperties;
	}

	protected SidecarConsulDiscoveryProperties getConsulDiscoveryProperties() {
		return consulDiscoveryProperties;
	}

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	protected HeartbeatProperties getHeartbeatProperties() {
		return heartbeatProperties;
	}

	protected List<ConsulManagementRegistrationCustomizer> getManagementRegistrationCustomizers() {
		return managementRegistrationCustomizers;
	}

	protected List<ConsulRegistrationCustomizer> getRegistrationCustomizers() {
		return registrationCustomizers;
	}

	protected HeartbeatProperties getDelegateHeartbeatProperties() {
		return delegateHeartbeatProperties;
	}

}
