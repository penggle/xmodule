package com.penglecode.xmodule.common.security.servlet.filter;

import org.springframework.security.web.FilterInvocation;

public interface FilterSecurityInterceptPredicate {

	/**
	 * 测试是否应用当前FilterSecurityInterceptor
	 * @param fi
	 * @return
	 */
	public boolean apply(FilterInvocation fi);
	
}
