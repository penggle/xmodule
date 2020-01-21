package com.penglecode.xmodule.common.exception;

/**
 * 应用服务接口异常Exception
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月28日 下午2:53:59
 * @version  	1.0
 */
public class ApplicationServiceApiException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	public ApplicationServiceApiException(String message) {
		super(message);
	}

	public ApplicationServiceApiException(Throwable cause) {
		super(cause);
	}

	public ApplicationServiceApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationServiceApiException(String code, String message) {
		super(code, message);
	}
	
	public ApplicationServiceApiException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
	
}
