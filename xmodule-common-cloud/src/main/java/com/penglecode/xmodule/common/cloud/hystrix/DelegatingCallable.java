package com.penglecode.xmodule.common.cloud.hystrix;

import java.util.concurrent.Callable;

import org.springframework.util.Assert;

/**
 * 代理的Callable
 * 
 * 解决将主线程的请求上下文参数添加到hystrix执行子线程中去
 * 
 * @param <V>
 * @author 	pengpeng
 * @date 	2020年2月12日 下午8:54:09
 */
public class DelegatingCallable<V> implements Callable<V> {

	private final Callable<V> delegate;
	
	private final HystrixConcurrencyContext context;
	
	public DelegatingCallable(Callable<V> delegate, HystrixConcurrencyContext context) {
		super();
		Assert.notNull(delegate, "Parameter 'delegate' can not be null!");
		Assert.notNull(context, "Parameter 'context' can not be null!");
		this.delegate = delegate;
		this.context = context;
	}

	@Override
	public V call() throws Exception {
		try {
			HystrixConcurrencyContextHolder.setContext(context);
			return delegate.call();
		} finally {
			HystrixConcurrencyContextHolder.resetContext();
		}
	}

}
