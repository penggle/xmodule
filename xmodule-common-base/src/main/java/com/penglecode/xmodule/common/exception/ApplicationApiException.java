package com.penglecode.xmodule.common.exception;

/**
 * 应用服务接口异常Exception
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月28日 下午2:53:59
 * @version  	1.0
 */
public class ApplicationApiException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	public ApplicationApiException(String message) {
		super(message);
	}

	public ApplicationApiException(Throwable cause) {
		super(cause);
	}

	public ApplicationApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationApiException(String code, String message) {
		super(code, message);
	}
	
	public ApplicationApiException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
	
}
