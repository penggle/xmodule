package com.penglecode.xmodule.common.cloud.support;

import java.util.Arrays;
import java.util.List;

/**
 * SpringCloud微服务之间相互调用的安全配置
 * 
 * @author 	pengpeng
 * @date	2019年6月13日 下午6:56:43
 */
public class CloudSecurityConfigProperties {

	private List<String> authExcludesUrl = Arrays.asList("/oauth/**");

	public List<String> getAuthExcludesUrl() {
		return authExcludesUrl;
	}

	public void setAuthExcludesUrl(List<String> authExcludesUrl) {
		this.authExcludesUrl = authExcludesUrl;
	}
	
}
