package com.penglecode.xmodule.security.oauth2.examples.support;

import java.net.URL;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.consul.discovery.SidecarDelegateHealthChecker;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * openapi异构微服务接口的代理健康检测
 * 
 * @author 	pengpeng
 * @date 	2020年1月19日 下午3:10:13
 */
@Component
public class OpenApiDelegateHealthChecker implements SidecarDelegateHealthChecker {

	private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiDelegateHealthChecker.class);
	
	@Resource(name="defaultRestTemplate")
	private RestTemplate restTemplate;
	
	@Override
	public boolean isInstanceHealthy(String instanceId, URL instanceUrl) throws Throwable {
		boolean health = checkHealth(instanceUrl);
		LOGGER.info(">>> 检测应用健康状况：health = {}, instanceId = {}, instanceUrl = {}", health, instanceId, instanceUrl);
		return health;
	}
	
	protected boolean checkHealth(URL instanceUrl) {
		String url = instanceUrl.toString() + "/test";
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, String.class);
		if(response.getStatusCode().is2xxSuccessful()) {
			return true;
		} else {
			LOGGER.error(">>> 检测应用健康状况出错：{} - {}", response.getStatusCode(), response.getBody());
			return false;
		}
	}

}
