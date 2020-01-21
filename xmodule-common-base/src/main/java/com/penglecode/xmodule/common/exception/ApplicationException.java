package com.penglecode.xmodule.common.exception;

import com.penglecode.xmodule.common.consts.GlobalConstants;

/**
 * 应用异常基类
 * 
 * @author	  	pengpeng
 * @date	  	2014年11月3日 上午11:26:00
 * @version  	1.0
 */
public abstract class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String code = String.valueOf(GlobalConstants.RESULT_CODE_FAILURE);
	
	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(String code, String message) {
		super(message);
		setCode(code);
	}
	
	public ApplicationException(String code, String message, Throwable cause) {
		super(message, cause);
		setCode(code);
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
