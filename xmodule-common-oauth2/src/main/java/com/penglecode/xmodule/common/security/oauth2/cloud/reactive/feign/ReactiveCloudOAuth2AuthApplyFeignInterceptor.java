package com.penglecode.xmodule.common.security.oauth2.cloud.reactive.feign;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest.Builder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

import com.penglecode.xmodule.common.security.oauth2.cloud.feign.AbstractCloudOAuth2AuthApplyFeignInterceptor;
import com.penglecode.xmodule.common.support.UnsupportedOperationExceptionInvocationHandler;

import feign.RequestTemplate;

/**
 * 微服务之间接口调用鉴权之基于OAuth2认证的AccessToken拦截器
 * 
 * 即在需要API鉴权的接口请求之前设置header[Authorization]
 * 
 * @author 	pengpeng
 * @date 	2020年2月12日 下午1:36:17
 */
public class ReactiveCloudOAuth2AuthApplyFeignInterceptor extends AbstractCloudOAuth2AuthApplyFeignInterceptor {

	@Autowired
	private ReactiveOAuth2AuthorizedClientManager authorizedClientManager;
	
	@Override
	protected OAuth2AuthorizeRequest buildAuthorizeRequest(RequestTemplate template, Builder builder) {
		builder.attributes(attributes -> {
			attributes.put(ServerWebExchange.class.getName(), new DummyServerWebExchange());
		});
		return builder.build();
	}
	
	@Override
	protected OAuth2AuthorizedClient authorize(OAuth2AuthorizeRequest authorizeRequest) {
		return authorizedClientManager.authorize(authorizeRequest).block();
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