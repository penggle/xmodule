package com.penglecode.xmodule.common.support;

import com.penglecode.xmodule.common.consts.ApplicationConstants;

/**
 * 国际化消息Holder
 * 
 * @author 	pengpeng
 * @date	2019年2月14日 下午12:42:43
 */
public class MessageHolder {

	/** 国际化资源文件中的消息code */
	private String msgCode;
	
	/** 消息参数列表 */
	private Object[] args;

	private MessageHolder(String msgCode) {
		this.msgCode = msgCode;
	}

	private MessageHolder(String msgCode, Object[] args) {
		this.msgCode = msgCode;
		this.args = args;
	}

	public String getMessage() {
		return ApplicationConstants.DEFAULT_MESSAGE_SOURCE_ACCESSOR.get().getMessage(msgCode, args);
	}

}