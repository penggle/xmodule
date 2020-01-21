package com.penglecode.xmodule.common.web.support;

import java.util.Map;

import org.springframework.http.MediaType;

/**
 * 对Http请求的日志记录
 * 
 * @param <T>
 * @author	  	pengpeng
 * @date	  	2014年10月17日 下午7:24:39
 * @version  	1.0
 */
public class HttpAccessLog<T> {

	/**
	 * 日志标题
	 */
	private String title;
	
	/**
	 * 所属功能模块
	 */
	private String module;
	
	/**
	 * 请求URI
	 */
	private String uri;
	
	/**
	 * 请求对应的MVC框架的控制器方法
	 */
	private String mvcMethod;
	
	/**
	 * 请求方法(GET/POST/PUT/DELETE/INPUT)
	 */
	private String httpMethod;
	
	/**
	 * 请求头
	 */
	private Map<String,String> requestHeader;
	
	/**
	 * 请求体类型
	 */
	private MediaType requestContentType;
	
	/**
	 * 请求参数
	 */
	private HttpRequestParameter requestParameter;
	
	/**
	 * 访问者
	 */
	private T accessUser;
	
	/**
	 * 访问时间
	 */
	private String accessTime;
	
	/**
	 * 访问者ip地址
	 */
	private String clientIpAddr;
	
	/**
	 * 被访问的服务器地址+端口号
	 */
	private String serverIpAddr;

	/**
	 * 访问开始系统毫秒数
	 */
	private Long accessBeginMillis;
	
	/**
	 * 访问结束系统毫秒数
	 */
	private Long accessEndMillis;
	
	/**
	 * 控制器方法的执行时长(毫秒)
	 */
	private Long processTime;
	
	/**
	 * 日志记录是否结束
	 */
	private boolean loggingCompleted = false;

	/**
	 * 请求是否是异步的
	 */
	private boolean asynRequest = true;
	
	/**
	 * 响应体类型
	 */
	private MediaType responseContentType;
	
	/**
	 * 响应结果
	 */
	private Object responseResult;
	
	/**
	 * 所属应用ID
	 */
	private Long appId;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMvcMethod() {
		return mvcMethod;
	}

	public void setMvcMethod(String mvcMethod) {
		this.mvcMethod = mvcMethod;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public Map<String, String> getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(Map<String, String> requestHeader) {
		this.requestHeader = requestHeader;
	}

	public MediaType getRequestContentType() {
		return requestContentType;
	}

	public void setRequestContentType(MediaType requestContentType) {
		this.requestContentType = requestContentType;
	}

	public HttpRequestParameter getRequestParameter() {
		return requestParameter;
	}

	public void setRequestParameter(HttpRequestParameter requestParameter) {
		this.requestParameter = requestParameter;
	}

	public T getAccessUser() {
		return accessUser;
	}

	public void setAccessUser(T accessUser) {
		this.accessUser = accessUser;
	}

	public String getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(String accessTime) {
		this.accessTime = accessTime;
	}

	public String getClientIpAddr() {
		return clientIpAddr;
	}

	public void setClientIpAddr(String clientIpAddr) {
		this.clientIpAddr = clientIpAddr;
	}

	public String getServerIpAddr() {
		return serverIpAddr;
	}

	public void setServerIpAddr(String serverIpAddr) {
		this.serverIpAddr = serverIpAddr;
	}

	public Long getAccessBeginMillis() {
		return accessBeginMillis;
	}

	public void setAccessBeginMillis(Long accessBeginMillis) {
		this.accessBeginMillis = accessBeginMillis;
	}

	public Long getAccessEndMillis() {
		return accessEndMillis;
	}

	public void setAccessEndMillis(Long accessEndMillis) {
		this.accessEndMillis = accessEndMillis;
	}

	public Long getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Long processTime) {
		this.processTime = processTime;
	}

	public boolean isLoggingCompleted() {
		return loggingCompleted;
	}

	public void setLoggingCompleted(boolean loggingCompleted) {
		this.loggingCompleted = loggingCompleted;
	}

	public boolean isAsynRequest() {
		return asynRequest;
	}

	public void setAsynRequest(boolean asynRequest) {
		this.asynRequest = asynRequest;
	}

	public MediaType getResponseContentType() {
		return responseContentType;
	}

	public void setResponseContentType(MediaType responseContentType) {
		this.responseContentType = responseContentType;
	}

	public Object getResponseResult() {
		return responseResult;
	}

	public void setResponseResult(Object responseResult) {
		this.responseResult = responseResult;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	@Override
	public String toString() {
		return "HttpAccessLog [title=" + title + ", module=" + module + ", uri=" + uri + ", mvcMethod=" + mvcMethod
				+ ", httpMethod=" + httpMethod + ", requestHeader=" + requestHeader + ", requestContentType="
				+ requestContentType + ", requestParameter=" + requestParameter + ", accessUser=" + accessUser
				+ ", accessTime=" + accessTime + ", clientIpAddr=" + clientIpAddr + ", serverIpAddr=" + serverIpAddr
				+ ", accessBeginMillis=" + accessBeginMillis + ", accessEndMillis=" + accessEndMillis + ", processTime="
				+ processTime + ", loggingCompleted=" + loggingCompleted + ", asynRequest=" + asynRequest
				+ ", responseContentType=" + responseContentType + ", responseResult=" + responseResult + ", appId="
				+ appId + "]";
	}

	public static class HttpRequestParameter {
		
		/**
		 * 请求参数
		 */
		private Map<String,String> parameter;
		
		/**
		 * 请求体
		 */
		private Object body;

		public Map<String, String> getParameter() {
			return parameter;
		}

		public void setParameter(Map<String, String> parameter) {
			this.parameter = parameter;
		}

		public Object getBody() {
			return body;
		}

		public void setBody(Object body) {
			this.body = body;
		}

		public String toString() {
			return "[parameter=" + parameter + ", body=" + body + "]";
		}
		
	}

}
