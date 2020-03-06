package com.penglecode.xmodule.springboot.examples.dynamicbean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractOpennessApiService implements InitializingBean, DisposableBean {

	private final String nodeName;
	
	@Autowired
	private RestTemplate restTemplate;

	public AbstractOpennessApiService(String nodeName) {
		super();
		this.nodeName = nodeName;
	}

	protected String getNodeName() {
		return nodeName;
	}
	
	protected RestTemplate getRestTemplate() {
		return restTemplate;
	}

	@Override
	public void destroy() throws Exception {
		System.out.println(String.format("【%s】>>> destroy()", getNodeName()));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println(String.format("【%s】>>> afterPropertiesSet()", getNodeName()));
	}
	
}
