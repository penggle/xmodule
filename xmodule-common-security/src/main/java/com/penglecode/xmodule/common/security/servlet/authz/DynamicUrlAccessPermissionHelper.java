package com.penglecode.xmodule.common.security.servlet.authz;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.penglecode.xmodule.common.exception.ApplicationRuntimeException;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.SpringWebMvcUtils;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * 动态URL访问权限检测帮助类
 * 
 * @author 	pengpeng
 * @date	2019年7月26日 上午9:39:05
 */
public class DynamicUrlAccessPermissionHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicUrlAccessPermissionHelper.class);
	
	private DynamicUrlSecurityMetadataSource securityMetadataSource;
	
	private DynamicUrlAccessDecisionManager accessDecisionManager;
	
	/**
	 * 判断当前用户是否有权限指定的请求(URL + HttpMethod)
	 * @param request
	 * @return
	 */
	public boolean hasAccessPermission(HttpServletRequest request) {
		Collection<ConfigAttribute> attributes = securityMetadataSource.getAttributes(request);
		Authentication authenticated = SecurityContextHolder.getContext().getAuthentication();
		if (!CollectionUtils.isEmpty(attributes)) {
			try {
				accessDecisionManager.decide(authenticated, request, attributes);
			} catch (AccessDeniedException e) {
				LOGGER.error(String.format("%s : [url = %s, method = %s]", e.getMessage(), request.getServletPath(), request.getMethod()));
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断当前用户是否有权限指定的请求(URL + HttpMethod)
	 * @param request
	 * @return
	 */
	public boolean hasAccessPermission(String url, String method) {
		HttpServletRequest request = SpringWebMvcUtils.getCurrentHttpServletRequest();
		String contextPath = request.getContextPath();
		URL urlObj = null;
		if(!url.toLowerCase().startsWith("http://")) {
			String requestUrl = request.getRequestURL().toString();
			String prefix = requestUrl.substring(0, requestUrl.indexOf('/', "https://".length() + 1));
			if(!url.startsWith("/")) {
				url = "/" + url;
			}
			url = prefix + url;
		}
		try {
			urlObj = new URL(url);
		} catch (MalformedURLException e) {
			throw new ApplicationRuntimeException(e);
		}
		String requestUrl = urlObj.getPath();
		if(!StringUtils.isEmpty(contextPath)) { //去掉requestUrl中的contextPath
			int index = requestUrl.indexOf('/', 1);
			if(index > 0 && contextPath.equals(requestUrl.substring(0, index))) {
				requestUrl = requestUrl.substring(index);
			}
		}
		String queryString = urlObj.getQuery();
		return hasAccessPermission(new DynamicUrlHttpServletRequest(request, requestUrl, queryString, method));
	}

	protected DynamicUrlSecurityMetadataSource getSecurityMetadataSource() {
		return securityMetadataSource;
	}

	public void setSecurityMetadataSource(DynamicUrlSecurityMetadataSource securityMetadataSource) {
		this.securityMetadataSource = securityMetadataSource;
	}

	protected DynamicUrlAccessDecisionManager getAccessDecisionManager() {
		return accessDecisionManager;
	}

	public void setAccessDecisionManager(DynamicUrlAccessDecisionManager accessDecisionManager) {
		this.accessDecisionManager = accessDecisionManager;
	}
	
}
