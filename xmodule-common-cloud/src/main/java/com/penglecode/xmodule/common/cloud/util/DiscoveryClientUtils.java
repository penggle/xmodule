package com.penglecode.xmodule.common.cloud.util;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.SpringUtils;

/**
 * 服务发现工具类
 * 
 * @author 	pengpeng
 * @date	2019年7月3日 下午12:40:30
 */
public class DiscoveryClientUtils {

	/**
	 * 判断指定服务是否可用
	 * @param serviceId
	 * @return
	 */
	public static boolean isServiceAvailable(String serviceId) {
		List<ServiceInstance> serviceInstances = getDiscoveryClient().getInstances(serviceId);
		return !CollectionUtils.isEmpty(serviceInstances);
	}
	
	protected static DiscoveryClient getDiscoveryClient() {
		DiscoveryClient discoveryClient = SpringUtils.getBean(DiscoveryClient.class);
		Assert.notNull(discoveryClient, String.format("No bean found of %s", DiscoveryClient.class));
		return discoveryClient;
	}
	
}
