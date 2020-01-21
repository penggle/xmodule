package org.springframework.cloud.openfeign;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.annotation.Scope;
import org.springframework.util.Assert;

import feign.hystrix.FallbackFactory;

/**
 * 默认的服务服务熔断|降级实现工厂类，
 * 
 * 通过Cglib动态代理来实现统一的服务熔断|降级逻辑
 * 
 * (注：该类在Spring容器中必须以@Scope("prototype")存在!!!)
 * 
 * @author 	pengpeng
 * @date	2019年6月1日 下午5:12:21
 */
@Scope("prototype")
public class DefaultHystrixFallbackFactory implements FallbackFactory<FallbackableFeignClient> {

	private Class<?> feignClientClass;
	
	private HystrixFallbackHandlerFactory fallbackHandlerFactory;
	
	@Override
	public FallbackableFeignClient create(Throwable cause) {
		//在create之前需要设置feignClientClass，见#
		Assert.notNull(feignClientClass, "Property 'feignClientClass' must be required!");
		Assert.notNull(fallbackHandlerFactory, "Property 'fallbackHandlerFactory' must be required!");
		return doCreate(cause);
	}
	
	protected FallbackableFeignClient doCreate(Throwable cause) {
		Enhancer enhancer = new Enhancer();
		enhancer.setClassLoader(Thread.currentThread().getContextClassLoader());
		enhancer.setSuperclass(getFeignClientClass());
		enhancer.setCallback(getFallbackHandlerFactory().createHandler(getFeignClientClass(), cause));
        return (FallbackableFeignClient) enhancer.create();
	}
	
	public HystrixFallbackHandlerFactory getFallbackHandlerFactory() {
		return fallbackHandlerFactory;
	}

	public void setFallbackHandlerFactory(HystrixFallbackHandlerFactory fallbackHandlerFactory) {
		this.fallbackHandlerFactory = fallbackHandlerFactory;
	}

	public Class<?> getFeignClientClass() {
		return feignClientClass;
	}

	public void setFeignClientClass(Class<?> feignClientClass) {
		this.feignClientClass = feignClientClass;
	}

}
