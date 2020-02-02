package com.penglecode.xmodule.common.security.servlet.util;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.penglecode.xmodule.common.util.ReflectionUtils;
import com.penglecode.xmodule.common.util.SpringUtils;

/**
 * spring-security工具类
 * 
 * @author 	pengpeng
 * @date	2019年1月16日 下午2:38:43
 */
@SuppressWarnings("unchecked")
public class SpringSecurityUtils {

	private static final Object mutex = new Object();
	
	private static volatile HttpSecurity primaryHttpSecurity;
	
	/**
	 * 获取SpringSecurity的配置bean
	 * 例如：
	 * @Primary
	 * @Configuration
	 * @EnableWebSecurity
	 * public class XxxSecurityConfiguration extends WebSecurityConfigurerAdapter {
	 * 		...
	 * }
	 * 
	 * @return
	 */
	public static WebSecurityConfigurerAdapter getPrimarySecurityConfigurer() {
		return SpringUtils.getBean(WebSecurityConfigurerAdapter.class);
	}
	
	/**
	 * 获取SpringSecurity的HttpSecurity配置
	 * @return
	 */
	public static HttpSecurity getPrimaryHttpSecurity() {
		WebSecurityConfigurerAdapter primarySecurityConfigurer = getPrimarySecurityConfigurer();
		if(primaryHttpSecurity == null) {
			synchronized (mutex) {
				if(primaryHttpSecurity == null) {
					Method method = ReflectionUtils.findMethod(WebSecurityConfigurerAdapter.class, "getHttp");
					method.setAccessible(true);
					primaryHttpSecurity = (HttpSecurity) ReflectionUtils.invokeMethod(method, primarySecurityConfigurer);
				}
			}
		}
		return primaryHttpSecurity;
	}
	
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
	 * 获取当前登录证明(Authentication)
	 * @param <T>
	 * @return
	 */
	public static <T extends Authentication> T getAuthentication() {
		return (T) SecurityContextHolder.getContext().getAuthentication();
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
	 * 执行退出登录
	 * @param request
	 * @param response
	 */
	public static void logout(HttpServletRequest request, HttpServletResponse response) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
	}
	
}
