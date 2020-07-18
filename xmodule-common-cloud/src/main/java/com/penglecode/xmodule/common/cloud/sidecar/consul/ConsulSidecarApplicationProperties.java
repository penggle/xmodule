package com.penglecode.xmodule.common.cloud.sidecar.consul;

import java.net.URL;
import java.time.Duration;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 异构应用的sidecar配置
 * 
 * @author 	pengpeng
 * @date	2020年1月8日 下午9:09:58
 */
public class ConsulSidecarApplicationProperties {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsulSidecarApplicationProperties.class);
	
	private static final String DEFAULT_HEARTBEAT_INTERVAL = "30s";
	
	/**
	 * 异构微服务的名称
	 */
	private String serviceName;
	
	/**
	 * 异构微服务的URL列表
	 * Set<http://ip:port>
	 */
	private Set<ServiceInstance> serviceInstances;
	
	/**
	 * 异构微服务的健康检查频率,默认10s
	 */
	private String instanceHealthCheckInterval = "10s";
	
	/**
	 * 异构微服务自身提供的健康检查请求路径
	 */
	private String instanceHealthCheckPath;
	
	/**
	 * 当异构微服务本身无法提供健康检查URL(instanceHealthCheckPath)时，退而求其次改用sidecar微服务代理其进行TTL健康检测机制
	 * (这种情况很少见，比如某个异构微服务的所有接口URL都需要登录才能访问，那么只能启用该方式来代理异构微服务进行健康检测)
	 */
	private boolean delegateHeartbeatEnabled = false;
	
	/**
	 * sidecar微服务代理异构微服务进行TTL健康检测的频率,默认30s
	 */
	private String delegateHeartbeatInterval = DEFAULT_HEARTBEAT_INTERVAL;
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Set<ServiceInstance> getServiceInstances() {
		return serviceInstances;
	}

	public void setServiceInstances(Set<ServiceInstance> serviceInstances) {
		this.serviceInstances = serviceInstances;
	}

	public String getInstanceHealthCheckInterval() {
		return instanceHealthCheckInterval;
	}

	public void setInstanceHealthCheckInterval(String instanceHealthCheckInterval) {
		this.instanceHealthCheckInterval = instanceHealthCheckInterval;
	}

	public String getInstanceHealthCheckPath() {
		return instanceHealthCheckPath;
	}

	public void setInstanceHealthCheckPath(String instanceHealthCheckPath) {
		this.instanceHealthCheckPath = instanceHealthCheckPath;
	}

	public boolean isDelegateHeartbeatEnabled() {
		return delegateHeartbeatEnabled;
	}

	public void setDelegateHeartbeatEnabled(boolean delegateHeartbeatEnabled) {
		this.delegateHeartbeatEnabled = delegateHeartbeatEnabled;
	}

	public String getDelegateHeartbeatInterval() {
		return delegateHeartbeatInterval;
	}

	public void setDelegateHeartbeatInterval(String delegateHeartbeatInterval) {
		this.delegateHeartbeatInterval = normalizeDelegateHeartbeatInterval(delegateHeartbeatInterval);
	}
	
	public Duration getParsedDelegateHeartbeatInterval() {
		return Duration.parse("PT" + delegateHeartbeatInterval);
	}
	
	/**
	 * 统一使用秒作单位的Interval表示形式
	 * @param delegateHeartbeatInterval
	 * @return
	 */
	protected String normalizeDelegateHeartbeatInterval(String delegateHeartbeatInterval) {
		Duration intervalDuration = null;
		String intervalText = delegateHeartbeatInterval;
		if(StringUtils.hasText(intervalText)) {
			if(!intervalText.startsWith("PT")) {
				intervalText = "PT" + intervalText;
				try {
					intervalDuration = Duration.parse(intervalText);
				} catch (Exception e) {
					LOGGER.error("Unparsable config 'delegateHeartbeatInterval': {}, the default heartbeat interval ({}) will be used instead!", delegateHeartbeatInterval, DEFAULT_HEARTBEAT_INTERVAL);
				}
			}
		}
		if(intervalDuration == null) {
			intervalText = "PT" + DEFAULT_HEARTBEAT_INTERVAL;
			intervalDuration = Duration.parse(intervalText);
		}
		return intervalDuration.getSeconds() + "s";
	}

	@Override
	public String toString() {
		return "{serviceName=" + serviceName + ", serviceInstances="
				+ serviceInstances + ", instanceHealthCheckInterval=" + instanceHealthCheckInterval
				+ ", instanceHealthCheckPath=" + instanceHealthCheckPath + ", delegateHeartbeatEnabled="
				+ delegateHeartbeatEnabled + ", delegateHeartbeatInterval=" + delegateHeartbeatInterval + "}";
	}


	public static class ServiceInstance {
		
		/**
		 * sidecar异构微服务的实例ID
		 */
		private String instanceId;
		
		/**
		 * sidecar异构微服务的实例URL
		 */
		private URL instanceUrl;
		
		/**
		 * 是否注册该sidecar异构微服务
		 */
		private boolean register = true;

		public String getInstanceId() {
			return instanceId;
		}

		public void setInstanceId(String instanceId) {
			this.instanceId = instanceId;
		}

		public URL getInstanceUrl() {
			return instanceUrl;
		}

		public void setInstanceUrl(URL instanceUrl) {
			this.instanceUrl = instanceUrl;
		}

		public boolean isRegister() {
			return register;
		}

		public void setRegister(boolean register) {
			this.register = register;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			ServiceInstance that = (ServiceInstance) o;
			return instanceUrl.equals(that.instanceUrl);
		}

		@Override
		public int hashCode() {
			return Objects.hash(instanceUrl);
		}

		@Override
		public String toString() {
			return "{instanceId=" + instanceId + ", instanceUrl=" + instanceUrl + ", register="
					+ register + "}";
		}
		
	}
	
}
