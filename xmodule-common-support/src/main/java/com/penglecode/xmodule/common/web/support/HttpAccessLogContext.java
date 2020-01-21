package com.penglecode.xmodule.common.web.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;


/**
 * Http访问日志上下文
 * 
 * @author 	pengpeng
 * @date	2018年8月14日 上午11:18:30
 */
public class HttpAccessLogContext {
		
	private final MvcResourceMethodMapping mvcResourceMethodMapping;
	
	private final HttpAccessLogging httpAccessLogging;
	
	private final HttpAccessLog<?> httpAccessLog;
	
	private final Map<Object,Object> attributes = new HashMap<Object,Object>();

	public HttpAccessLogContext(MvcResourceMethodMapping mvcResourceMethodMapping, HttpAccessLogging httpAccessLogging, HttpAccessLog<?> httpAccessLog) {
		super();
		Assert.notNull(mvcResourceMethodMapping, "Parameter 'mvcResourceMethodMapping' can not be null!");
		Assert.notNull(httpAccessLogging, "Parameter 'httpAccessLogging' can not be null!");
		Assert.notNull(httpAccessLog, "Parameter 'httpAccessLog' can not be null!");
		this.mvcResourceMethodMapping = mvcResourceMethodMapping;
		this.httpAccessLogging = httpAccessLogging;
		this.httpAccessLog = httpAccessLog;
	}

	public MvcResourceMethodMapping getMvcResourceMethodMapping() {
		return mvcResourceMethodMapping;
	}

	public HttpAccessLogging getHttpAccessLogging() {
		return httpAccessLogging;
	}

	public HttpAccessLog<?> getHttpAccessLog() {
		return httpAccessLog;
	}

	public Map<Object, Object> getAttributes() {
		return attributes;
	}
	
}