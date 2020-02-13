package com.penglecode.xmodule.common.cloud.feign.oauth2;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

import com.penglecode.xmodule.common.cloud.hystrix.HystrixConcurrencyContext;
import com.penglecode.xmodule.common.cloud.hystrix.HystrixConcurrencyContextHolder;
import com.penglecode.xmodule.common.support.UnsupportedOperationExceptionInvocationHandler;

import feign.RequestTemplate;
import reactor.core.publisher.Mono;

/**
 * 微服务之间接口调用鉴权之基于OAuth2认证的AccessToken拦截器
 * 
 * 即在需要API鉴权的接口请求之前设置header[Authorization]
 * 
 * @author 	pengpeng
 * @date 	2020年2月12日 下午1:36:17
 */
@SuppressWarnings("unchecked")
public class ReactiveCloudOAuth2AuthApplyFeignInterceptor extends AbstractCloudOAuth2AuthApplyFeignInterceptor {

	@Autowired
	private ReactiveOAuth2AuthorizedClientManager authorizedClientManager;
	
	@Override
	protected OAuth2AuthorizedClient authorize(OAuth2AuthorizeRequest authorizeRequest) {
		return authorizedClientManager.authorize(authorizeRequest).block();
	}
	
	@Override
	protected Map<String,Object> authorizeRequestAttributes(RequestTemplate template) {
		Map<String,Object> attributes = new HashMap<String,Object>();
		HystrixConcurrencyContext context = HystrixConcurrencyContextHolder.getContext();
		Mono<ServerWebExchange> exchangeMono = null;
		ServerWebExchange exchange = null;
		if(context == null || (exchangeMono = (Mono<ServerWebExchange>) context.get(ServerWebExchange.class.getName())) == null) {
			exchange = exchangeMono.defaultIfEmpty(new DummyServerWebExchange()).block();
		}
		attributes.put(ServerWebExchange.class.getName(), exchange);
		return attributes;
	}

	protected ReactiveOAuth2AuthorizedClientManager getAuthorizedClientManager() {
		return authorizedClientManager;
	}

	static class DummyServerWebExchange extends ServerWebExchangeDecorator {

		private static final ServerWebExchange UNSUPPORTED_EXCHANGE = (ServerWebExchange) Proxy
				.newProxyInstance(DummyServerWebExchange.class.getClassLoader(),
						new Class[] { ServerWebExchange.class },
						new UnsupportedOperationExceptionInvocationHandler());


		public DummyServerWebExchange() {
			super(UNSUPPORTED_EXCHANGE);
		}
		
	}
	
}