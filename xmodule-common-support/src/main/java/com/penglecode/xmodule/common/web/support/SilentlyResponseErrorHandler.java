package com.penglecode.xmodule.common.web.support;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * 不抛出异常的ResponseErrorHandler
 * 
 * @author 	pengpeng
 * @date	2019年7月2日 下午6:07:57
 */
public class SilentlyResponseErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return false;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		//nothing to do
	}

}
