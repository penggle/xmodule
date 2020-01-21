package com.penglecode.xmodule.common.web.springmvc.view;

/**
 * 输出javascript类型的View
 * 
 * @author 	pengpeng
 * @date	2018年5月31日 下午4:45:45
 */
public class TextJavascriptView extends PlainTextView {

	public TextJavascriptView() {
		this(DEFAULT_CHARSET);
	}

	public TextJavascriptView(String charset) {
		super(charset);
		setContentType("application/javascript;charset=" + getCharset());
	}
	
}