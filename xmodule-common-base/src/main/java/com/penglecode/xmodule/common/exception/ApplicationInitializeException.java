package com.penglecode.xmodule.common.exception;
/**
 * 应用程序初始化异常
 * 
 * @author  pengpeng
 * @date 	 2019年4月22日 上午10:57:35
 * @version 1.0
 */
public class ApplicationInitializeException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	public ApplicationInitializeException(String message) {
		super(message);
	}

	public ApplicationInitializeException(Throwable cause) {
		super(cause);
	}

	public ApplicationInitializeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ApplicationInitializeException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}

	public ApplicationInitializeException(String code, String message) {
		super(code, message);
	}

	/**
	 * Return the detail message, including the message from the nested exception
	 * if there is one.
	 */
	public String getMessage() {
		return buildMessage(super.getMessage(), getCause());
	}


	/**
	 * Retrieve the innermost cause of this exception, if any.
	 * @return the innermost exception, or {@code null} if none
	 * @since 2.0
	 */
	public Throwable getRootCause() {
		Throwable rootCause = null;
		Throwable cause = getCause();
		while (cause != null && cause != rootCause) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}
	
	/**
	 * Build a message for the given base message and root cause.
	 * @param message the base message
	 * @param cause the root cause
	 * @return the full exception message
	 */
	protected String buildMessage(String message, Throwable cause) {
		if (cause != null) {
			StringBuilder sb = new StringBuilder();
			if (message != null) {
				sb.append(message).append("; ");
			}
			sb.append("nested exception is ").append(cause);
			return sb.toString();
		}
		else {
			return message;
		}
	}
	
}
