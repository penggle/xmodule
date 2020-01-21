package com.penglecode.xmodule.common.security.servlet.authz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 动态指定URL的HttpServletRequestWrapper
 * 
 * @author 	pengpeng
 * @date	2019年7月26日 上午11:00:49
 */
public class DynamicUrlHttpServletRequest extends HttpServletRequestWrapper {

	private final String requestUrl;
	
	private final String method;
	
	private final String queryString;
	
	public DynamicUrlHttpServletRequest(HttpServletRequest request, String requestUrl, String queryString, String method) {
		super(request);
		Assert.hasText(requestUrl, "Parameter 'requestUrl' can not be empty!");
		Assert.hasText(method, "Parameter 'method' can not be empty!");
		this.requestUrl = requestUrl;
		this.queryString = queryString;
		this.method = method;
	}

	@Override
	public String getMethod() {
		return method;
	}

	@Override
	public String getPathInfo() {
		return null;
	}

	@Override
	public String getRequestURI() {
		String uri = getServletPath();
		String contextPath = getContextPath();
		if(StringUtils.hasText(contextPath)) {
			uri = contextPath + uri;
		}
		return uri;
	}

	@Override
	public StringBuffer getRequestURL() {
		String url = super.getRequestURL().toString();
		url = url.substring(0, url.indexOf('/', "https://".length() + 1)) + getRequestURI();
		return new StringBuffer(url);
	}

	@Override
	public String getServletPath() {
		return requestUrl;
	}

	@Override
	public String getQueryString() {
		return queryString;
	}
	
}
