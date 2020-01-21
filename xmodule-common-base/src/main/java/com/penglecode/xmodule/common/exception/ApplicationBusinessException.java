package com.penglecode.xmodule.common.exception;

/**
 * 用于业务的Exception,可以设置业务异常代码
 * 
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月28日 下午2:53:59
 * @version  	1.0
 */
public class ApplicationBusinessException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	public ApplicationBusinessException(String message) {
		super(message);
	}

	public ApplicationBusinessException(Throwable cause) {
		super(cause);
	}

	public ApplicationBusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationBusinessException(String code, String message) {
		super(code, message);
	}
	
	public ApplicationBusinessException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}

}
