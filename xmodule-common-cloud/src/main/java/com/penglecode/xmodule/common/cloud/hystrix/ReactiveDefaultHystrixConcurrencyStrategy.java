package com.penglecode.xmodule.common.cloud.hystrix;

import org.springframework.web.server.ServerWebExchange;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.penglecode.xmodule.common.util.SpringWebFluxUtils;

/**
 * 自定义策略
 * 解决将主线程的请求上下文参数(HttpServletRequest,HttpServletResponse)添加到hystrix执行子线程中去
 * 
 * @author 	pengpeng
 * @date 	2020年2月12日 下午9:06:58
 */
public class ReactiveDefaultHystrixConcurrencyStrategy extends AbstractHystrixConcurrencyStrategy {

	public ReactiveDefaultHystrixConcurrencyStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy) {
		super(existingConcurrencyStrategy);
	}

	@Override
	protected HystrixConcurrencyContext detectContext() {
		HystrixConcurrencyContext context = HystrixConcurrencyContextHolder.getContext();
		if(context == null) {
			context = new HystrixConcurrencyContext();
		}
		
		//请求上下文
		context.put(ServerWebExchange.class.getName(), SpringWebFluxUtils.getCurrentServerWebExchange());
		return context;
	}
	
}
