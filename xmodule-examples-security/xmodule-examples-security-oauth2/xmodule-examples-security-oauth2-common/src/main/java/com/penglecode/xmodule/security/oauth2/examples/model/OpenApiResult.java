package com.penglecode.xmodule.security.oauth2.examples.model;

import org.springframework.http.HttpStatus;

import com.penglecode.xmodule.common.support.Result;

public class OpenApiResult<T> {

	private int code;
	
	private String message;
	
	private T result;

	public OpenApiResult() {
		super();
	}

	public OpenApiResult(int code, String message, T result) {
		super();
		this.code = code;
		this.message = message;
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
	
	public Result<T> toResult() {
		HttpStatus statusCode = HttpStatus.resolve(code);
		if(statusCode != null && statusCode.is2xxSuccessful()) {
			return Result.success().code(code).message(message).data(result).build();
		} else {
			return Result.failure().code(code).message(message).data(result).build();
		}
	}	
	
}