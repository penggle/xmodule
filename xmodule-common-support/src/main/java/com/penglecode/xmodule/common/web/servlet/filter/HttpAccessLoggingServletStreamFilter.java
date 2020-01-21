package com.penglecode.xmodule.common.web.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.penglecode.xmodule.common.util.SpringUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.support.HttpAccessLogging;

/**
 * Http访问日志记录之Servlet输入输出流过滤器,解决：
 * 1、HttpServletRequest的输入流可重读,
 * 2、HttpServletResponse的输出流可缓存
 * 
 * @author 	pengpeng
 * @date   		2017年5月16日 下午12:47:22
 * @version 	1.0
 */
public class HttpAccessLoggingServletStreamFilter extends OncePerRequestFilter {

	private final Object mutex = new Object();
	
	private volatile boolean initialized = false;
	
	private AbstractHandlerMethodMapping<?> springMvcHandlerMethodMapping;
	
	public HttpAccessLoggingServletStreamFilter() {
		super();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest requestToUse = request;
		HttpServletResponse responseToUse = response;
		if(isRequestSupport(request)){
			requestToUse = new ContentCachingRequestWrapper(request);
			responseToUse = new ContentCachingResponseWrapper(response);
		}
		filterChain.doFilter(requestToUse, responseToUse);
		ContentCachingResponseWrapper contentCachedResponse = getContentCachingResponseWrapper(responseToUse);
		if(contentCachedResponse != null){
			contentCachedResponse.copyBodyToResponse(); //重写响应到response.OutputStream中去,否则客户端响应会出现NO CONTENT
		}
	}

	/**
	 * 请求是否支持访问日志记录
	 * @param request
	 * @return
	 */
	protected boolean isRequestSupport(HttpServletRequest request) {
		try {
			if(!this.isAsyncDispatch(request) && !this.isAsyncStarted(request)){
				HandlerExecutionChain handlerExecutionChain = this.getSpringMvcHandlerMethodMapping().getHandler(request);
				if(handlerExecutionChain != null && handlerExecutionChain.getHandler() != null && handlerExecutionChain.getHandler() instanceof HandlerMethod){
					HandlerMethod handlerMethod = (HandlerMethod) handlerExecutionChain.getHandler();
					HttpAccessLogging httpAccessLogging = handlerMethod.getMethodAnnotation(HttpAccessLogging.class);
					return httpAccessLogging != null;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}
	
	/**
	 * 获取ContentCachingResponseWrapper
	 * (考虑到Wrapper是可以重复的,故需要递归获取)
	 * @param response
	 * @return
	 */
	protected ContentCachingResponseWrapper getContentCachingResponseWrapper(ServletResponse response) {
		if(response instanceof ContentCachingResponseWrapper){
			return (ContentCachingResponseWrapper) response;
		}else if(response instanceof HttpServletResponseWrapper) {
			HttpServletResponseWrapper responseToUse = (HttpServletResponseWrapper) response;
			return getContentCachingResponseWrapper(responseToUse.getResponse());
		}
		return null;
	}
	
	protected MediaType getContentType(String contentType) {
		try {
			if(!StringUtils.isEmpty(contentType)){
				return MediaType.valueOf(contentType);
			}
		} catch (Exception e) {
		}
		return null;
	}

	public AbstractHandlerMethodMapping<?> getSpringMvcHandlerMethodMapping() {
		if(!this.initialized){
			initSpringMvcHandlerMethodMapping();
		}
		return springMvcHandlerMethodMapping;
	}

	protected void initSpringMvcHandlerMethodMapping() {
		if(!this.initialized){
			synchronized (mutex) {
				if(!this.initialized){
					this.springMvcHandlerMethodMapping = SpringUtils.getBean(AbstractHandlerMethodMapping.class);
					this.initialized = true;
				}
			}
		}
	}
	
}
