package org.springframework.cloud.gateway.support;

/**
 * 强制gatew重试的Exception
 * 
 * @author 	pengpeng
 * @date	2020年1月10日 下午7:15:14
 */
public class ForceRetryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ForceRetryException() {
		super();
	}

	public ForceRetryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ForceRetryException(String message, Throwable cause) {
		super(message, cause);
	}

	public ForceRetryException(String message) {
		super(message);
	}

	public ForceRetryException(Throwable cause) {
		super(cause);
	}

}
