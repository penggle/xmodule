package com.penglecode.xmodule.rabbitmq.examples.exception;

public class RabbitMqException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RabbitMqException() {
		super();
	}

	public RabbitMqException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RabbitMqException(String message, Throwable cause) {
		super(message, cause);
	}

	public RabbitMqException(String message) {
		super(message);
	}

	public RabbitMqException(Throwable cause) {
		super(cause);
	}

}
