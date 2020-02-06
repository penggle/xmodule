package com.penglecode.xmodule.common.security.servlet.support;

import org.springframework.security.core.userdetails.UserDetails;

import com.penglecode.xmodule.common.security.servlet.util.SpringSecurityUtils;
import com.penglecode.xmodule.common.web.servlet.support.HttpApiResourceSupport;

/**
 * 基于SpringSecurity权限认证的HTTP API接口辅助基类
 * 
 * @author 	pengpeng
 * @date	2019年12月25日 下午2:36:55
 */
@SuppressWarnings("unchecked")
public abstract class SecurityHttpApiResourceSupport extends HttpApiResourceSupport {

	/**
	 * 获取当前登录用户
	 * @return
	 */
	
	protected <T extends UserDetails> T getCurrentAuthenticatedUser() {
		return (T) SpringSecurityUtils.getCurrentAuthenticatedUser();
	}
	
}
