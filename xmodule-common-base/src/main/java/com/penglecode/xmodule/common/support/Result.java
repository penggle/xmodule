package com.penglecode.xmodule.common.support;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.exception.ApplicationApiException;

/**
 * 通用返回结果类
 *
 * @param <T>
 * @author pengpeng
 * @version 1.0
 * @date 2014年6月13日 上午8:59:37
 */
public class Result<T> implements DtoModel {

	private static final long serialVersionUID = 1L;

	/** 成功与否 */
    private boolean success;

    /** 结果代码 */
    private int code;
    
    /** 消息 */
    private String message;
    
    /** 结果数据 */
    private T data;
    
	Result() {
	}

	Result(boolean success, int code, String message, T data) {
		this.success = success;
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	Result(Result<T> result) {
		this.success = result.isSuccess();
		this.code = result.getCode();
		this.message = result.getMessage();
		this.data = result.getData();
	}

	public boolean isSuccess() {
		return success;
	}

	protected void setSuccess(boolean success) {
		this.success = success;
	}

	public int getCode() {
		return code;
	}

	protected void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	protected void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}
	
	protected void setData(T data) {
		this.data = data;
	}

	public T get() {
		if(success) {
			return data;
		} else {
			String message = this.message;
			if(message == null || message.trim().equals("")) {
				message = "Unkown Error";
			}
		}
		throw new ApplicationApiException(message);
	}
	
	public static Builder success() {
		return new Builder(Boolean.TRUE, GlobalConstants.RESULT_CODE_SUCCESS, "OK");
	}
	
	public static Builder failure() {
		return new Builder(Boolean.FALSE, GlobalConstants.RESULT_CODE_FAILURE, "Internal Server Error");
	}
	
	public String toString() {
		return "Result [success=" + success + ", code=" + code + ", message="
				+ message + ", data=" + data + "]";
	}
	
	public static class Builder {
		
	    private boolean success = true;

	    private int code = GlobalConstants.RESULT_CODE_SUCCESS;
	    
	    private String message;
	    
	    private Object data;

		Builder(boolean success, int code, String message) {
			this.success = success;
			this.code = code;
			this.message = message;
		}
		
		public Builder code(int code) {
			this.code = code;
			return this;
		}
		
		public Builder message(String message) {
			this.message = message;
			return this;
		}
		
		public Builder data(Object data) {
			this.data = data;
			return this;
		}
		
		@SuppressWarnings("unchecked")
		public <T> Result<T> build() {
			return new Result<T>(success, code, message, (T) data);
		}
	    
	}
	
}
