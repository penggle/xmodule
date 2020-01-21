package com.penglecode.xmodule.common.util;

import java.util.regex.Pattern;

import com.google.common.base.Throwables;

/**
 * 异常消息工具类
 * 
 * @author	  	pengpeng
 * @date	  	2014年11月1日 下午11:08:50
 * @version  	1.0
 */
public class ExceptionUtils {
	
	private static final Pattern CHINESE_CHAR_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

	/**
	 * 获取根源异常
	 * @param th
	 * @return
	 */
	public static Throwable getRootCause(Throwable th) {
        Throwable root = Throwables.getRootCause(th);
        root = root == null ? th : root;
        return root;
    }

	/**
	 * 获取根源异常消息
	 * @param th
	 * @return
	 */
	public static String getRootCauseMessage(Throwable th) {
        return getRootCause(th).getMessage();
    }
	
	/**
	 * 获取异常堆栈消息
	 * @param th
	 * @return
	 */
	public static String getStackTrace(Throwable th) {
		return Throwables.getStackTraceAsString(th);
	}
	
	/**
	 * 异常信息是否包含中文字符
	 * @param message
	 * @return
	 */
	public static boolean isContainsChineseChar(String message){
		if(message != null && CHINESE_CHAR_PATTERN.matcher(message).find()){
			return true;
		}
		return false;
	}
	
}
