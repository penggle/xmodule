package com.penglecode.xmodule.common.cloud.sidecar.consul;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.cloud.sidecar.consul.ConsulSidecarApplicationProperties.ServiceInstance;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.util.URIUtils;

/**
 * sidecar异构微服务自动注册与发现配置
 * 
 * @author 	pengpeng
 * @date	2020年1月9日 下午4:06:11
 */
public class SidecarConsulDiscoveryProperties extends ConsulDiscoveryProperties implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(SidecarConsulDiscoveryProperties.class);
	
	private ConsulSidecarApplicationProperties sidecar;
	
	private InetUtils inetUtils;
	
	private final List<ConsulDiscoveryProperties> sidecarConsulDiscoveryConfigs = new ArrayList<>();
	
	public SidecarConsulDiscoveryProperties(InetUtils inetUtils) {
		super(inetUtils);
		this.inetUtils = inetUtils;
	}

	public ConsulSidecarApplicationProperties getSidecar() {
		return sidecar;
	}

	public void setSidecar(ConsulSidecarApplicationProperties sidecar) {
		this.sidecar = sidecar;
	}
	
	public InetUtils getInetUtils() {
		return inetUtils;
	}

	public void setInetUtils(InetUtils inetUtils) {
		this.inetUtils = inetUtils;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(sidecar != null && !CollectionUtils.isEmpty(sidecar.getServiceInstances())) {
			LOGGER.info("Sidecar application registration configuration : {}", sidecar);
			sidecar.getServiceInstances().forEach(serviceInstance -> {
				sidecarConsulDiscoveryConfigs.add(createSidecarConsulDiscoveryProperties(serviceInstance));
			});
		}
		Assert.notEmpty(sidecarConsulDiscoveryConfigs, "No sidecar service provider configuration found! Please check property: spring.cloud.consul.discovery.sidecar.*");
	}

	/**
	 * 创建指定sidecar异构微服务的ConsulDiscoveryProperties
	 * @param serviceInstance
	 * @return
	 */
	protected ConsulDiscoveryProperties createSidecarConsulDiscoveryProperties(ServiceInstance serviceInstance) {
		String scheme = StringUtils.defaultIfEmpty(serviceInstance.getInstanceUrl().getProtocol(), getScheme());
		String serviceName = sidecar.getServiceName();
		Assert.hasText(serviceName, "sidecar.serviceName must be required!");
		Assert.isTrue(!serviceName.equals(getServiceName()), "current.serviceName must be different with sidecar.serviceName!");
		String instanceId = StringUtils.defaultIfEmpty(serviceInstance.getInstanceId(), serviceName + "-" + serviceInstance.getInstanceUrl().getHost() + "-" + serviceInstance.getInstanceUrl().getPort());
		
		String healthCheckUrl = null;
		if(sidecar.isDelegateHeartbeatEnabled()) { //否则sidecar需要实现一个#HeartbeatTask来代理异构微服务的进行TTL健康检查
			//优先使用TTL健康检查 @see org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration.createCheck(Integer, HeartbeatProperties, ConsulDiscoveryProperties)
		} else if(!StringUtils.isEmpty(sidecar.getInstanceHealthCheckPath())) { //如果指定了异构微服务自身提供的健康检查URL
			//其次使用healthCheckUrl
			healthCheckUrl = String.format("%s://%s:%s%s", scheme, serviceInstance.getInstanceUrl().getHost(), URIUtils.getPort(serviceInstance.getInstanceUrl()), sidecar.getInstanceHealthCheckPath());
		} else {
			//如果都没有设置则报错
			Assert.state(false, "Config property 'instanceHealthCheckPath' or 'delegateHeartbeatEnabled' must set one of them!");
		}
		
		ConsulDiscoveryProperties properties = new ConsulDiscoveryProperties(inetUtils);
		
		properties.setAclToken(getAclToken());
		properties.setCatalogServicesWatchDelay(getCatalogServicesWatchDelay());
		properties.setCatalogServicesWatchTimeout(getCatalogServicesWatchTimeout());
		properties.setConsistencyMode(getConsistencyMode());
		properties.setDatacenters(getDatacenters());
		properties.setDefaultQueryTag(getDefaultQueryTag());
		properties.setDefaultZoneMetadataName(getDefaultZoneMetadataName());
		properties.setDeregister(isDeregister());
		properties.setEnabled(isEnabled());
		properties.setFailFast(isFailFast());
		
		if(!StringUtils.isEmpty(healthCheckUrl)) {
			properties.setHealthCheckCriticalTimeout(getHealthCheckCriticalTimeout());
			properties.setHealthCheckHeaders(getHealthCheckHeaders());
			properties.setHealthCheckInterval(StringUtils.defaultIfEmpty(sidecar.getInstanceHealthCheckInterval(), getHealthCheckInterval())); //设置健康检查周期
			properties.setHealthCheckPath(sidecar.getInstanceHealthCheckPath()); //设置健康检查路径
			properties.setHealthCheckTimeout(getHealthCheckTimeout());
			properties.setHealthCheckTlsSkipVerify(getHealthCheckTlsSkipVerify());
			properties.setHealthCheckUrl(healthCheckUrl); //强制使用自定义的健康检测URL
		}
		
		properties.setHostname(serviceInstance.getInstanceUrl().getHost());
		properties.setIncludeHostnameInInstanceId(isIncludeHostnameInInstanceId());
		properties.setInstanceGroup(getInstanceGroup());
		properties.setInstanceId(instanceId);
		properties.setInstanceZone(getInstanceZone());
		properties.setIpAddress(serviceInstance.getInstanceUrl().getHost());
		properties.setLifecycle(getLifecycle());
		properties.setManagementPort(getManagementPort());
		properties.setManagementSuffix(getManagementSuffix());
		properties.setManagementTags(getManagementTags());
		properties.setOrder(getOrder());
		properties.setPort(URIUtils.getPort(serviceInstance.getInstanceUrl()));
		properties.setPreferAgentAddress(isPreferAgentAddress());
		properties.setPreferIpAddress(isPreferIpAddress());
		properties.setQueryPassing(isQueryPassing());
		properties.setRegister(serviceInstance.isRegister());
		properties.setRegisterHealthCheck(true);
		properties.setScheme(scheme);
		properties.setServerListQueryTags(getServerListQueryTags());
		properties.setServiceName(serviceName);
		properties.setTags(getTags());
		return properties;
	}
	
	public List<ConsulDiscoveryProperties> getSidecarConsulDiscoveryConfigs() {
		return sidecarConsulDiscoveryConfigs;
	}

}
