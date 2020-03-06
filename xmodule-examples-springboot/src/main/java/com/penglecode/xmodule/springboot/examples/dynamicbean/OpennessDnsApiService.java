package com.penglecode.xmodule.springboot.examples.dynamicbean;

public class OpennessDnsApiService extends AbstractOpennessApiService {

	public OpennessDnsApiService(String nodeName) {
		super(nodeName);
	}

	public void setDns(String dns) {
		System.out.println(String.format("【%s】>>> setDns: %s", getNodeName(), dns));
	}
	
}
