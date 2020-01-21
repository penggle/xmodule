package com.penglecode.xmodule.common.security.servlet.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

/**
 * spring-security工具类
 * 
 * @author 	pengpeng
 * @date	2019年1月16日 下午2:38:43
 */
public class SpringSecurityUtils {

	/**
	 * 获取认证(登录)异常
	 * @param request
	 * @return
	 */
	public static Exception getAuthenticationException(HttpServletRequest request) {
		String key = WebAttributes.AUTHENTICATION_EXCEPTION;
		Exception exception = null;
		if(exception == null) {
			exception = (Exception) request.getAttribute(key);
		}
		if(exception == null) {
			exception = (Exception) request.getSession().getAttribute(key);
			if(exception != null) {
				request.getSession().removeAttribute(key);
			}
		}
		return exception;
	}
	
	/**
	 * 获取当前登录用户
	 * @return
	 */
	public static UserDetails getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
			return (UserDetails) authentication.getPrincipal();
		}
		return null;
	}
	
	/**
	 * 执行退出登录最后一步清除Authentication
	 * @param request
	 * @param response
	 */
	public static void clearAuthentication(HttpServletRequest request, HttpServletResponse response) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
	}
	
}
