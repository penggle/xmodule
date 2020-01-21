package com.penglecode.xmodule.common.support;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.exception.ApplicationException;
import com.penglecode.xmodule.common.util.ExceptionUtils;

/**
 * 模块内异常解析逻辑
 * 
 * @author	  	pengpeng
 * @date	  	2015年11月1日 下午11:08:50
 * @version  	1.0
 */
public class ExceptionDescriptorResolver {
	
	public static ExceptionDescriptor resolveException(Throwable ex) {
		return resolveException(ex, "message.request.failure");
	}
	
	public static ExceptionDescriptor resolveException(Throwable ex, String messageCode) {
		Throwable target = null;
		int code = GlobalConstants.RESULT_CODE_FAILURE;
		String message = null;
		boolean found = false;
		Throwable cause = ex;
		while(cause != null){
			if (cause instanceof ApplicationException) { // 已知的异常信息
				target = cause;
				try {
					code = Integer.valueOf(((ApplicationException) cause).getCode());
				} catch (Exception e) {
				}
				message = cause.getMessage();
				if(ExceptionUtils.isContainsChineseChar(message)){
					found = true;
					break;
				}
			}
			cause = cause.getCause();
		}
		if(!found){
			target = ExceptionUtils.getRootCause(ex);
			message = target.getMessage();
			if(!ExceptionUtils.isContainsChineseChar(message)){
				message = ApplicationConstants.DEFAULT_MESSAGE_SOURCE_ACCESSOR.get().getMessage(messageCode, new Object[] {message}, message); // 未知的异常消息,需要转换成统一的,以增强用户体验
			}
		}
		return new ExceptionDescriptor(target, code, message);
	}
	
}
