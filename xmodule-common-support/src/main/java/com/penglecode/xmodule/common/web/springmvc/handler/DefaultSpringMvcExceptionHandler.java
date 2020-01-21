package com.penglecode.xmodule.common.web.springmvc.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.google.common.base.Throwables;
import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.support.Result;

/**
 * 默认的全局SpringMVC异常处理器
 * 
 * @author 	pengpeng
 * @date 	2020年1月17日 下午6:32:50
 */
public class DefaultSpringMvcExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpringMvcExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<Object> defaultExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        response.setStatus(status.value());
		return handleExceptionInternal(ex, null, new HttpHeaders(), status, new ServletWebRequest(request, response));
    }
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest req) {
		headers = headers == null ? new HttpHeaders() : headers;
		headers.setContentType(ApplicationConstants.DEFAULT_RESPONSE_CONTENT_TYPE);
		LOGGER.error(ex.getMessage(), ex);
		NativeWebRequest request = (NativeWebRequest) req;
		body = createExceptionResponseBody(request.getNativeRequest(HttpServletRequest.class), request.getNativeResponse(HttpServletResponse.class), ex, status);
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	protected Object createExceptionResponseBody(HttpServletRequest request, HttpServletResponse response, Throwable ex, HttpStatus status) {
		Throwable rootException = Throwables.getRootCause(ex);
		String message = rootException.getMessage();
		int code = status.value();
		return Result.failure().code(code).message(message).build();
	}
	
}
