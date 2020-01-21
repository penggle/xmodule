package com.penglecode.xmodule.common.exception;

/**
 * 不合法的数字签名异常
 * 
 * @author 	pengpeng
 * @date	2018年1月4日 下午2:51:11
 */
public class IllegalDigitalSignatureException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	public IllegalDigitalSignatureException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}

	public IllegalDigitalSignatureException(String code, String message) {
		super(code, message);
	}

	public IllegalDigitalSignatureException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalDigitalSignatureException(String message) {
		super(message);
	}

	public IllegalDigitalSignatureException(Throwable cause) {
		super(cause);
	}

}
