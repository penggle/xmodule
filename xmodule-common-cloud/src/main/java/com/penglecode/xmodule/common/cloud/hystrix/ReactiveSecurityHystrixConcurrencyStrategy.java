package com.penglecode.xmodule.common.cloud.hystrix;

import org.springframework.security.core.Authentication;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.penglecode.xmodule.common.security.reactive.util.SpringSecurityUtils;

/**
 * 自定义策略
 * 解决将主线程的请求上下文参数(HttpServletRequest,HttpServletResponse,Authentication)添加到hystrix执行子线程中去
 * 
 * @author 	pengpeng
 * @date 	2020年2月12日 下午9:06:58
 */
public class ReactiveSecurityHystrixConcurrencyStrategy extends ReactiveDefaultHystrixConcurrencyStrategy {

	public ReactiveSecurityHystrixConcurrencyStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy) {
		super(existingConcurrencyStrategy);
	}

	@Override
	protected HystrixConcurrencyContext detectContext() {
		HystrixConcurrencyContext context = super.detectContext();
		
		//spring-security
		context.put(Authentication.class.getName(), SpringSecurityUtils.getCurrentAuthentication());
		return context;
	}
	
}
