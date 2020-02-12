package com.penglecode.xmodule.common.security.oauth2.cloud.servlet.feign;

import java.lang.reflect.Proxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest.Builder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

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
public class ServletCloudOAuth2AuthApplyFeignInterceptor extends AbstractCloudOAuth2AuthApplyFeignInterceptor {
	
	@Autowired
	private OAuth2AuthorizedClientManager authorizedClientManager;
	
	@Override
	protected OAuth2AuthorizedClient authorize(OAuth2AuthorizeRequest authorizeRequest) {
		return authorizedClientManager.authorize(authorizeRequest);
	}

	@Override
	protected OAuth2AuthorizeRequest buildAuthorizeRequest(RequestTemplate template, Builder builder) {
		builder.attributes(attributes -> {
			attributes.put(HttpServletRequest.class.getName(), new DummyHttpServletRequest());
			attributes.put(HttpServletResponse.class.getName(), new DummyHttpServletResponse());
		});
		return builder.build();
	}

	protected OAuth2AuthorizedClientManager getAuthorizedClientManager() {
		return authorizedClientManager;
	}
	
	static class DummyHttpServletRequest extends HttpServletRequestWrapper {

		private static final HttpServletRequest UNSUPPORTED_REQUEST = (HttpServletRequest) Proxy
				.newProxyInstance(DummyHttpServletRequest.class.getClassLoader(),
						new Class[] { HttpServletRequest.class },
						new UnsupportedOperationExceptionInvocationHandler());


		public DummyHttpServletRequest() {
			super(UNSUPPORTED_REQUEST);
		}
		
	}
	
	static class DummyHttpServletResponse extends HttpServletResponseWrapper {

		private static final HttpServletResponse UNSUPPORTED_RESPONSE = (HttpServletResponse) Proxy
				.newProxyInstance(DummyHttpServletResponse.class.getClassLoader(),
						new Class[] { HttpServletResponse.class },
						new UnsupportedOperationExceptionInvocationHandler());
		
		public DummyHttpServletResponse() {
			super(UNSUPPORTED_RESPONSE);
		}

	}

}