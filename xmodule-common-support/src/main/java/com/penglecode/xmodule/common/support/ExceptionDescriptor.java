package com.penglecode.xmodule.common.support;

/**
 * 异常描述
 * 
 * @author 	pengpeng
 * @date	2019年7月3日 下午2:34:15
 */
public class ExceptionDescriptor {
		
	private Throwable target;
	
	private String message;
	
	private int code;

	public ExceptionDescriptor() {
		super();
	}

	public ExceptionDescriptor(Throwable target, int code, String message) {
		super();
		this.target = target;
		this.message = message;
		this.code = code;
	}

	public Throwable getTarget() {
		return target;
	}

	public void setTarget(Throwable target) {
		this.target = target;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Result<Object> toResult() {
		return Result.failure().code(code).message(message).build();
	}
	
}