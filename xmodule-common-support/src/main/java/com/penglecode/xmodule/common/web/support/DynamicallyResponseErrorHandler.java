package com.penglecode.xmodule.common.web.support;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;

/**
 * 通过RestTemplateSynchronizationManager来动态切换的ResponseErrorHandler
 * 
 * @author 	pengpeng
 * @date	2019年7月2日 下午7:30:12
 */
public class DynamicallyResponseErrorHandler implements ResponseErrorHandler {

	private final Map<Class<? extends ResponseErrorHandler>, ResponseErrorHandler> responseErrorHandlers;
	
	private final Class<? extends ResponseErrorHandler> defaultResponseErrorHandler;
	
	public DynamicallyResponseErrorHandler() {
		this(null, null);
	}

	public DynamicallyResponseErrorHandler(
			Map<Class<? extends ResponseErrorHandler>, ResponseErrorHandler> responseErrorHandlers) {
		this(null, responseErrorHandlers);
	}
	
	public DynamicallyResponseErrorHandler(Class<? extends ResponseErrorHandler> defaultResponseErrorHandler) {
		this(defaultResponseErrorHandler, null);
	}

	public DynamicallyResponseErrorHandler(
			Class<? extends ResponseErrorHandler> defaultResponseErrorHandler,
			Map<Class<? extends ResponseErrorHandler>, ResponseErrorHandler> responseErrorHandlers) {
		super();
		if(CollectionUtils.isEmpty(responseErrorHandlers)) {
			responseErrorHandlers = new HashMap<>();
			responseErrorHandlers.put(DefaultResponseErrorHandler.class, new DefaultResponseErrorHandler());
			responseErrorHandlers.put(SilentlyResponseErrorHandler.class, new SilentlyResponseErrorHandler());
		}
		if(!responseErrorHandlers.containsKey(DefaultResponseErrorHandler.class)) {
			responseErrorHandlers.put(DefaultResponseErrorHandler.class, new DefaultResponseErrorHandler());
		}
		responseErrorHandlers.remove(DynamicallyResponseErrorHandler.class); //avoid endless-loop
		this.defaultResponseErrorHandler = ObjectUtils.defaultIfNull(defaultResponseErrorHandler, DefaultResponseErrorHandler.class);
		this.responseErrorHandlers = responseErrorHandlers;
	}

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return getResponseErrorHandler().hasError(response);
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		getResponseErrorHandler().handleError(response);
	}
	
	protected ResponseErrorHandler getResponseErrorHandler() {
		Class<? extends ResponseErrorHandler> responseErrorHandlerType = RestTemplateSynchronizationManager.getCurrentResponseErrorHandlerType();
		if(responseErrorHandlerType == null) {
			responseErrorHandlerType = defaultResponseErrorHandler;
		}
		return responseErrorHandlers.get(responseErrorHandlerType);
	}

	protected Class<? extends ResponseErrorHandler> getDefaultResponseErrorHandler() {
		return defaultResponseErrorHandler;
	}

	protected Map<Class<? extends ResponseErrorHandler>, ResponseErrorHandler> getResponseErrorHandlers() {
		return responseErrorHandlers;
	}
	
}