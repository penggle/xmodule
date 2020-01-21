package com.penglecode.xmodule.common.web.springmvc.view;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

/**
 * 输出常规文本类型的View
 * 
 * @author 	pengpeng
 * @date	2018年5月31日 下午4:45:45
 */
public class PlainTextView extends AbstractView {

	public static final String DEFAULT_OUTPUT_CONTENT_MODEL_KEY = "output-content";
	
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	private String outputContentModelKey = DEFAULT_OUTPUT_CONTENT_MODEL_KEY;
	
	private final String charset;
	
	public PlainTextView() {
		this(DEFAULT_CHARSET);
	}

	public PlainTextView(String charset) {
		if(charset == null) {
			charset = DEFAULT_CHARSET;
		}
		this.charset = charset;
		setContentType("text/html;charset=" + getCharset());
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Object value = model.get(outputContentModelKey);
		if(value != null) {
			String textContent = value.toString();
			byte[] bytes = textContent.getBytes(charset);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(bytes, 0, bytes.length);
			writeToResponse(response, out);
		}
		
	}

	public String getOutputContentModelKey() {
		return outputContentModelKey;
	}

	public void setOutputContentModelKey(String outputContentModelKey) {
		this.outputContentModelKey = outputContentModelKey;
	}

	public String getCharset() {
		return charset;
	}

}