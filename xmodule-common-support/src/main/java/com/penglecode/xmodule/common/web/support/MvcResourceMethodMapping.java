package com.penglecode.xmodule.common.web.support;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.Assert;

import com.penglecode.xmodule.common.util.CollectionUtils;

public class MvcResourceMethodMapping {
	
	private final Class<?> resourceClass;
	
	private final Method resourceMethod;
	
	private final List<String> requestMethods;
	
	private final List<String> resourceUriPatterns;
	
	public MvcResourceMethodMapping(Class<?> resourceClass, Method resourceMethod, List<String> requestMethods, List<String> resourceUriPatterns) {
		super();
		Assert.notNull(resourceClass, "Parameter 'resourceClass' can not be null!");
		Assert.notNull(resourceMethod, "Parameter 'resourceMethod' can not be null!");
		Assert.notEmpty(resourceUriPatterns, "Parameter 'resourceUriPattern' can not be null!");
		this.resourceClass = resourceClass;
		this.resourceMethod = resourceMethod;
		this.resourceUriPatterns = resourceUriPatterns;
		this.requestMethods = CollectionUtils.isEmpty(requestMethods) ? Arrays.asList("GET") : requestMethods;
	}

	public Class<?> getResourceClass() {
		return resourceClass;
	}

	public Method getResourceMethod() {
		return resourceMethod;
	}

	public List<String> getRequestMethods() {
		return requestMethods;
	}

	public List<String> getResourceUriPatterns() {
		return resourceUriPatterns;
	}

	@Override
	public String toString() {
		return "MvcResourceMethodMapping [resourceMethod=" + resourceMethod
				+ ", requestMethods=" + requestMethods + ", resourceUriPatterns=" + resourceUriPatterns + "]";
	}

}