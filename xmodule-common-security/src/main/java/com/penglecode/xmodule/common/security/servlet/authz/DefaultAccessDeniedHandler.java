package com.penglecode.xmodule.common.security.servlet.authz;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.Assert;

/**
 * 默认的授权错误处理器
 * 
 * @author 	pengpeng
 * @date	2019年12月26日 上午10:13:04
 */
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAccessDeniedHandler.class);
	
	/**
	 * 未授权请求处理url
	 */
	private final String unauthorizedHandlerUrl;
	
	public DefaultAccessDeniedHandler(String unauthorizedHandlerUrl) {
		super();
		Assert.hasText(unauthorizedHandlerUrl, "Parameter 'unauthorizedHandlerUrl' must be required!");
		this.unauthorizedHandlerUrl = unauthorizedHandlerUrl;
	}
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessException) throws IOException, ServletException {
		LOGGER.error(">>> 授权出错, path = {}, {} = {}", request.getRequestURI(), accessException.getClass().getName(), accessException.getMessage());
		request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessException);
		RequestDispatcher dispatcher = request.getRequestDispatcher(unauthorizedHandlerUrl);
		dispatcher.forward(request, response);
	}

	protected String getUnauthorizedHandlerUrl() {
		return unauthorizedHandlerUrl;
	}
	
}
