package com.penglecode.xmodule.common.redis;

import com.penglecode.xmodule.common.exception.ApplicationRuntimeException;

public class RedisException extends ApplicationRuntimeException {

	private static final long serialVersionUID = 1L;

	public RedisException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}

	public RedisException(String code, String message) {
		super(code, message);
	}

	public RedisException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedisException(String message) {
		super(message);
	}

	public RedisException(Throwable cause) {
		super(cause);
	}

}
