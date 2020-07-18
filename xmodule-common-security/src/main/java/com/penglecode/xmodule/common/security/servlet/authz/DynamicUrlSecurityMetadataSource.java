package com.penglecode.xmodule.common.security.servlet.authz;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.penglecode.xmodule.common.security.consts.SecurityApplicationConstants;
import com.penglecode.xmodule.common.security.support.RbacRoleResource;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * 动态URL-ROLE权限MetadataSource
 * 
 * @author 	pengpeng
 * @date	2019年5月20日 下午1:45:22
 */
public class DynamicUrlSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicUrlSecurityMetadataSource.class);
	
	private volatile Map<RequestMatcher, Set<ConfigAttribute>> securityPermissions = new HashMap<RequestMatcher, Set<ConfigAttribute>>();
	
	/**
	 * 如果资源没有配置任何角色-资源关系时，则保险期间该资源应该赋予没人可以访问的权限
	 */
	private String supremeRole = "SUPERME";
	
	protected String getSupremeRole() {
		return supremeRole;
	}

	public void setSupremeRole(String supremeRole) {
		this.supremeRole = supremeRole;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		final HttpServletRequest request;
		if(object instanceof HttpServletRequest) {
			request = (HttpServletRequest) object;
		} else {
			request = ((FilterInvocation) object).getRequest();
		}
		 
        Collection<ConfigAttribute> configAttrs = null;
        for (Map.Entry<RequestMatcher, Set<ConfigAttribute>> entry : securityPermissions.entrySet()) {
            if (entry.getKey().matches(request)) {
            	configAttrs = entry.getValue();
                break;
            }
        }
        return configAttrs;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();
        for (Map.Entry<RequestMatcher, Set<ConfigAttribute>> entry : securityPermissions.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }
        return allAttributes;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	protected Map<RequestMatcher, Set<ConfigAttribute>> getSecurityPermissions() {
		return securityPermissions;
	}
	
	/**
	 * 该方法需要在角色-资源更新是时候被通知&更新
	 * @param allRoleResourceMappings
	 */
	public void setResourceRolesConfig(List<RbacRoleResource> allResourceRoles) {
		LOGGER.info(">>> 更新应用的角色-资源配置关系!");
		Map<RequestMatcher, Set<ConfigAttribute>> securityPermissions = new HashMap<RequestMatcher, Set<ConfigAttribute>>();
		if(!CollectionUtils.isEmpty(allResourceRoles)) {
			for(RbacRoleResource roleResource : allResourceRoles) {
				RequestMatcher requestMatcher = getRequestMatcher(roleResource.getResourceUrl(), roleResource.getHttpMethod());
				Set<ConfigAttribute> configAttrs = securityPermissions.computeIfAbsent(requestMatcher, k -> new LinkedHashSet<ConfigAttribute>());
				configAttrs.add(new SecurityConfig(prefixRole(StringUtils.defaultIfEmpty(roleResource.getRoleCode(), supremeRole))));
			}
		}
		for(Map.Entry<RequestMatcher, Set<ConfigAttribute>> entry : securityPermissions.entrySet()) {
			LOGGER.info(">>> {} : {}", entry.getKey(), entry.getValue());
		}
		this.securityPermissions = securityPermissions;
	}
	
	protected String prefixRole(String roleCode) {
		return SecurityApplicationConstants.DEFAULT_ROLE_VOTE_PREFIX + roleCode;
	}
	
	protected RequestMatcher getRequestMatcher(String url, String httpMethod) {
		if(httpMethod == null || HttpMethod.resolve(httpMethod) == null) {
			httpMethod = null;
		}
        return new AntPathRequestMatcher(url, httpMethod);
    }

}
