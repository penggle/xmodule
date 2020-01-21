package com.penglecode.xmodule.common.security.servlet.filter;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * 自定义的动态URL-ROLE权限拦截器
 * 重写invoke方法，invoke方法的重写逻辑完全copy自父类
 * 本类仅改了FILTER_APPLIED的值，从而避免后续FilterSecurityInterceptor能够被继续链式调用
 * 
 * @author 	pengpeng
 * @date	2018年10月26日 上午9:34:35
 */
public class DynamicUrlSecurityInterceptor extends FilterSecurityInterceptor {

	private static final String FILTER_APPLIED = "__spring_security_customFilterSecurityInterceptor_filterApplied";
	
	private FilterSecurityInterceptPredicate securityInterceptPredicate = new DefaultFilterSecurityInterceptPredicate();
	
	public void invoke(FilterInvocation fi) throws IOException, ServletException {
		if(securityInterceptPredicate != null && !securityInterceptPredicate.apply(fi)) {
			fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE); //不应用当前FilterSecurityInterceptor
		}
		if ((fi.getRequest() != null)
				&& (fi.getRequest().getAttribute(FILTER_APPLIED) != null)
				&& isObserveOncePerRequest()) {
			// filter already applied to this request and user wants us to observe
			// once-per-request handling, so don't re-do security checking
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		}
		else {
			// first time this request being called, so perform security checking
			if (fi.getRequest() != null && isObserveOncePerRequest()) {
				fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
			}

			InterceptorStatusToken token = super.beforeInvocation(fi);

			try {
				fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
			}
			finally {
				super.finallyInvocation(token);
			}

			super.afterInvocation(token, null);
		}
	}

	public FilterSecurityInterceptPredicate getSecurityInterceptPredicate() {
		return securityInterceptPredicate;
	}

	public void setSecurityInterceptPredicate(FilterSecurityInterceptPredicate securityInterceptPredicate) {
		this.securityInterceptPredicate = securityInterceptPredicate;
	}
	
	class DefaultFilterSecurityInterceptPredicate implements FilterSecurityInterceptPredicate {

		@Override
		public boolean apply(FilterInvocation fi) {
			return true;
		}
		
	}
	
}
