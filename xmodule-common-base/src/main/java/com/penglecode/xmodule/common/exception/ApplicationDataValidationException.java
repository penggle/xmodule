package com.penglecode.xmodule.common.exception;

/**
 * 用于数据校验的Exception
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月28日 下午2:53:59
 * @version  	1.0
 */
public class ApplicationDataValidationException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	public ApplicationDataValidationException(String message) {
		super(message);
	}

	public ApplicationDataValidationException(Throwable cause) {
		super(cause);
	}

	public ApplicationDataValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationDataValidationException(String code, String message) {
		super(code, message);
	}
	
	public ApplicationDataValidationException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
	
}
