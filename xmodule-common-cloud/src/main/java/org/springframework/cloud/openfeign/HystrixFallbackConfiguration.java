package org.springframework.cloud.openfeign;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Feign;

/**
 * 开启统一的服务熔断|降级配置，
 * 通过动态代理实现统一的fallback逻辑，
 * 没必要每个服务的每个方法都写一堆相同的逻辑的fallback
 * 
 * 需要做如下几步：
 * 
 * 1、编写服务的统一熔断|降级处理器及其工厂类，继承{@link #HystrixFallbackHandler}
 * 
 * public class MyHystrixFallbackHandler extends HystrixFallbackHandler {
 * 
 * 		public DefaultHystrixFallbackHandler(Class<?> feignClientClass, Throwable cause) {
 * 			super(feignClientClass, cause);
 * 		}
 * 
 * 		@Override
 * 		protected Object handle(Object proxy, Method method, Object[] args) throws Throwable {
 * 			Class<?> returnType = method.getReturnType();
 * 			if(PageResult.class.equals(returnType)) {
 * 				return HystrixFallbackResults.defaultFallbackPageResult();
 * 			} else {
 * 				return HystrixFallbackResults.defaultFallbackResult();
 * 			}
 * 		}
 * 
 * }
 * 
 * public class MyHystrixFallbackHandlerFactory implements HystrixFallbackHandlerFactory {
 * 
 * 		@Override
 * 		public HystrixFallbackHandler createHandler(Class<?> feignClientClass, Throwable cause) {
 * 			return new MyHystrixFallbackHandler(feignClientClass, cause);
 * 		}
 * 
 * }
 * 
 * 2、启用{@link #HystrixFallbackConfiguration}配置并注册{@link #DefaultHystrixFallbackFactory}
 *  
 *  @Configuration
 *  @ConditionalOnClass(Feign.class)
 *  @Import({HystrixFallbackConfiguration.class}) //启用配置
 *  public class MyFeignClientsConfiguration {
 *      
 *  	@Bean
 *  	@Scope("prototype")
 *  	public DefaultHystrixFallbackFactory defaultHystrixFallbackFactory() {
 *  		DefaultHystrixFallbackFactory defaultHystrixFallbackFactory = new DefaultHystrixFallbackFactory();
 *  		defaultHystrixFallbackFactory.setFallbackHandlerFactory(new MyHystrixFallbackHandlerFactory());
 *  		return defaultHystrixFallbackFactory;
 *  	}
 *  
 *  }
 *  
 * 3、在@FeignClient注解中使用fallbackFactory=DefaultHystrixFallbackFactory.class，并且被@FeignClient注解的服务接口需要实现{@link #FallbackableFeignClient}接口
 * 
 * 	@FeignClient(name="springcloud-nacos-producer", qualifier="productApiService", contextId="productApiService", fallbackFactory=DefaultHystrixFallbackFactory.class)
 * 	public interface ProductApiService extends FallbackableFeignClient {
 * 		
 * 		@GetMapping(value="/api/product/{productId}", produces=APPLICATION_JSON)
 * 		public Result<Product> getProductById(@PathVariable("productId") Long productId);
 * 	
 * 		...
 * 	}
 * 
 * @author 	pengpeng
 * @date	2019年6月1日 下午6:17:50
 */
@Configuration
@ConditionalOnClass(Feign.class)
public class HystrixFallbackConfiguration {

	/** 覆盖{@link #FeignAutoConfiguration}中的默认配置 */
	
	@Configuration
	@ConditionalOnClass(name = "feign.hystrix.HystrixFeign")
	protected static class HystrixFeignTargeterConfiguration {
		@Bean
		@ConditionalOnMissingBean
		public Targeter feignTargeter() {
			return new HystrixFallbackTargeter();
		}
	}
	
}
