package com.penglecode.xmodule.common.security.support;

import org.springframework.context.ApplicationEvent;

/**
 * Security权限配置信息(用户-角色-资源)变更的同步Event
 * 
 * @author 	pengpeng
 * @date	2018年6月4日 上午9:50:11
 */
public class SecurityPermsSynchronizeEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public SecurityPermsSynchronizeEvent(Object source) {
		super(source);
	}

}
