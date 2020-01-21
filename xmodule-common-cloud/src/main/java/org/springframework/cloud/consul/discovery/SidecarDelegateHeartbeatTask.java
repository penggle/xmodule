package org.springframework.cloud.consul.discovery;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryClient;
import org.springframework.util.Assert;

import com.ecwid.consul.v1.ConsulClient;

/**
 * sidecar服务代理异构微服务进行TTL健康检测的任务
 * 
 * @author 	pengpeng
 * @date 	2020年1月19日 下午1:14:42
 */
public class SidecarDelegateHeartbeatTask implements Runnable {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ConsulDiscoveryClient.class);
	
	private final String instanceId;
	
	private final String checkId;
	
	private final URL instanceUrl;
	
	private final ConsulClient client;
	
	private final SidecarDelegateHealthChecker healthChecker;
	
	public SidecarDelegateHeartbeatTask(ConsulClient client, String instanceId, URL instanceUrl, SidecarDelegateHealthChecker healthChecker) {
		Assert.hasText(instanceId, "Parameter 'instanceId' must be required!");
		Assert.notNull(instanceUrl, "Parameter 'instanceUrl' must be required!");
		Assert.notNull(client, "Parameter 'client' must be required!");
		Assert.notNull(healthChecker, "Parameter 'healthChecker' must be required!");
		this.client = client;
		this.instanceId = instanceId;
		this.checkId = "service:" + instanceId;
		this.instanceUrl = instanceUrl;
		this.healthChecker = healthChecker;
	}
	
	@Override
	public final void run() {
		if(isInstanceHealthy()) {
			client.agentCheckPass(checkId);
			LOGGER.debug("Sending consul heartbeat for: {}", instanceId);
		}
	}
	
	/**
	 * 检测异构微服务实例是否是健康的
	 * @return
	 */
	protected boolean isInstanceHealthy() {
		try {
			return healthChecker.isInstanceHealthy(instanceId, instanceUrl);
		} catch (Throwable e) {
			LOGGER.error(String.format("Checking instance[%s] health error: %s", instanceId, e.getMessage()), e);
		}
		return false;
	}

	protected String getInstanceId() {
		return instanceId;
	}

	protected String getCheckId() {
		return checkId;
	}

	protected URL getInstanceUrl() {
		return instanceUrl;
	}

	protected ConsulClient getClient() {
		return client;
	}

	protected SidecarDelegateHealthChecker getHealthChecker() {
		return healthChecker;
	}

}
