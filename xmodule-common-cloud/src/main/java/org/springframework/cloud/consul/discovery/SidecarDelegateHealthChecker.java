package org.springframework.cloud.consul.discovery;

import java.net.URL;

/**
 * sidecar服务代理异构微服务进行TTL健康检测
 * 
 * @author 	pengpeng
 * @date 	2020年1月19日 下午2:52:32
 */
public interface SidecarDelegateHealthChecker {

	/**
	 * 检测异构微服务实例是否是健康的
	 * @param instanceId
	 * @param instanceUrl
	 * @return
	 * @throws Throwable
	 */
	public boolean isInstanceHealthy(String instanceId, URL instanceUrl) throws Throwable;
	
}
