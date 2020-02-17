package com.penglecode.xmodule.keycloak.storage.user.exception;

public class KeycloakStorageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public KeycloakStorageException() {
		super();
	}

	public KeycloakStorageException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public KeycloakStorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public KeycloakStorageException(String message) {
		super(message);
	}

	public KeycloakStorageException(Throwable cause) {
		super(cause);
	}

}
