package com.penglecode.xmodule.common.web.support;

import java.io.IOException;
import java.nio.charset.Charset;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;

/**
 * 特殊处理返回结果为{@link #Result}类型的ResponseErrorHandler
 * 
 * @author 	pengpeng
 * @date	2019年7月3日 上午9:12:52
 */
public class ResultResponseErrorHandler extends DefaultResponseErrorHandler {

	private Charset defaultCharset = Charset.forName(GlobalConstants.DEFAULT_CHARSET);
	
	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		boolean hasError = super.hasError(response);
		if(hasError) {
			MediaType contentType = getContentType(response);
			return !isResultResponse(response, contentType);
		}
		return hasError;
	}
	
	/**
	 * 是否是{@link #Result}类型的响应
	 * @param response
	 * @param contentType
	 * @return
	 */
	protected boolean isResultResponse(ClientHttpResponse response, MediaType contentType) {
		if(contentType != null && contentType.includes(MediaType.APPLICATION_JSON)) {
			String json = new String(getResponseBody(response), getCharset(response));
			if(JsonUtils.isJsonObject(json)) {
				JSONObject jsonObject = new JSONObject(json);
				return jsonObject.has("success") && jsonObject.has("code") && jsonObject.has("message") && jsonObject.has("data");
			}
		}
		return false;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		super.handleError(response);
	}

	protected MediaType getContentType(ClientHttpResponse response) {
		HttpHeaders headers = response.getHeaders();
		return headers.getContentType();
	}
	
	@Override
	protected Charset getCharset(ClientHttpResponse response) {
		Charset charset = super.getCharset(response);
		return ObjectUtils.defaultIfNull(charset, getDefaultCharset());
	}

	public Charset getDefaultCharset() {
		return defaultCharset;
	}

	public void setDefaultCharset(Charset defaultCharset) {
		this.defaultCharset = defaultCharset;
	}

}
