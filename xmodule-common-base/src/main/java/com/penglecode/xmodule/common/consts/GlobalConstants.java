package com.penglecode.xmodule.common.consts;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.penglecode.xmodule.common.support.GlobalAppConfig;

/**
 * 全局共通常量
 * 
 * @author  pengpeng
 * @date 	 2015年4月18日 下午5:50:07
 * @version 1.0
 */
public class GlobalConstants {

	/**
	 * 默认字符编码
	 */
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	/**
	 * 默认Locale
	 */
	public static final Locale DEFAULT_LOCALE = new Locale("zh", "CN");
	
	
	/**
	 * 系统默认的上传文件临时存储路径
	 */
	public static final String DEFAULT_UPLOAD_TEMP_SAVE_PATH = "/upload/temp/";
	
	/**
	 * 系统默认的允许上传图片格式
	 */
	public static final List<String> DEFAULT_UPLOAD_IMAGE_FORMATS = Arrays.asList("jpg", "jpeg", "png");
	
	/**
	 * 系统默认的允许上传EXCEL格式
	 */
	public static final List<String> DEFAULT_UPLOAD_EXCEL_FORMATS = Arrays.asList("xls", "xlsx");
	
	/**
	 * 默认的上传图片长宽允许的误差(默认10px)
	 */
	public static final Integer DEFAULT_UPLOAD_IMAGE_PIXEL_DEVIATION = 10;
	
	/**
	 * 返回结果之成功
	 */
	public static final int RESULT_CODE_SUCCESS = 200;

	/**
	 * 返回结果之失败
	 */
	public static final int RESULT_CODE_FAILURE = 500;

	/**
	 * 针对数据库字段,诸如:'是','真','已删除',...等等由数字"1"代表的真值
	 */
	public static final String DEFAULT_YES_TRUE_FLAG = "1";

	/**
	 * 针对数据库字段,诸如:'否','假','未删除',...等等由数字"0"代表的假值
	 */
	public static final String DEFAULT_NO_FALSE_FLAG = "0";
	
	/**
	 * 默认的超级管理员用户ID
	 */
	public static final long DEFAULT_SUPER_ADMIN_USER_ID = 1;
	
	/**
	 * 默认的微服务客户端ID
	 */
	public static final Long DEFAULT_CLOUD_CLIENT_USER_ID = 0L;
	
	/**
	 * 默认的应用ID
	 */
	public static final Long DEFAULT_APPLICATION_ID = 1L;
	
	/**
	 * 全局应用配置
	 */
	public static final GlobalAppConfig GLOBAL_APP_CONFIG = new GlobalAppConfig();
	
}
