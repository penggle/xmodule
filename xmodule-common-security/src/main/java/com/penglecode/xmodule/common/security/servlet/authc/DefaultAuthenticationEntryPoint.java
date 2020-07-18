package com.penglecode.xmodule.common.security.servlet.authc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.WebAttributes;
import org.springframework.util.Assert;

/**
 * 默认的AuthenticationEntryPoint
 * 
 * @author 	pengpeng
 * @date	2019年12月26日 上午9:50:26
 */
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAuthenticationEntryPoint.class);
	
	/**
	 * 未认证请求处理url
	 */
	private final String unauthenticatedHandlerUrl;
	
	public DefaultAuthenticationEntryPoint(String unauthenticatedHandlerUrl) {
		super();
		Assert.hasText(unauthenticatedHandlerUrl, "Parameter 'unauthenticatedHandlerUrl' must be required!");
		this.unauthenticatedHandlerUrl = unauthenticatedHandlerUrl;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		LOGGER.error(">>> 认证出错, path = {}, {} = {}", request.getRequestURI(), authException.getClass().getName(), authException.getMessage());
		request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, authException);
		request.getRequestDispatcher(unauthenticatedHandlerUrl).forward(request, response);
	}

	protected String getUnauthenticatedHandlerUrl() {
		return unauthenticatedHandlerUrl;
	}

}
