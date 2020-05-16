package com.penglecode.xmodule.platform.upms.consts;

import com.penglecode.xmodule.common.consts.Constant;
import com.penglecode.xmodule.common.consts.SpringEnvConstant;

/**
 * 统一用户权限管理系统常量
 * 
 * @author 	pengpeng
 * @date	2019年5月18日 上午11:40:52
 */
public class UpmsConstants {
	
	/**
	 * 系统默认的上传文件临时存储路径
	 */
	public static final String DEFAULT_USER_ICON_SAVE_PATH = "/img/usericon/";
	
	/**
	 * 用户默认头像
	 */
	public static final Constant<String> DEFAULT_USER_AVATAR = new SpringEnvConstant<String>("visp.upms.defaultUserAvatar", "/img/default-user-avatar.png") {};
	
	/**
	 * 默认的资源ICON
	 */
	public static final Constant<String> DEFAULT_RESOURCE_ICON = new SpringEnvConstant<String>("visp.upms.defaultResourceIcon" ,"fas fa-dot") {};
	
	/**
	 * 默认的应用ICON
	 */
	public static final Constant<String> DEFAULT_APPLICATION_ICON = new SpringEnvConstant<String>("visp.upms.defaultApplicationIcon" ,"fas fa-cloud") {};
	
}
