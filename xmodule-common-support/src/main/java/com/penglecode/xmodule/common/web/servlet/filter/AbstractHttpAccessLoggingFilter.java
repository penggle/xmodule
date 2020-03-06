package com.penglecode.xmodule.common.web.servlet.filter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.support.NamedThreadFactory;
import com.penglecode.xmodule.common.util.ArrayUtils;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.NetUtils;
import com.penglecode.xmodule.common.util.ServletWebUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.support.HttpAccessLog;
import com.penglecode.xmodule.common.web.support.HttpAccessLog.HttpRequestParameter;
import com.penglecode.xmodule.common.web.support.HttpAccessLogContext;
import com.penglecode.xmodule.common.web.support.HttpAccessLogDAO;
import com.penglecode.xmodule.common.web.support.HttpAccessLogging;
import com.penglecode.xmodule.common.web.support.MvcResourceMethodMapping;

/**
 * Http访问日志记录之Servlet输入输出流过滤器,解决：
 * 1、HttpServletRequest的输入流可重读,
 * 2、HttpServletResponse的输出流可缓存
 * 
 * @author 	pengpeng
 * @date   		2017年5月16日 下午12:47:22
 * @version 	1.0
 */
@SuppressWarnings("unchecked")
public abstract class AbstractHttpAccessLoggingFilter extends OncePerRequestFilter implements ApplicationContextAware {

	public static final Pattern MESSAGE_SOURCE_CODE_PATTERN = Pattern.compile("\\$\\{([a-zA-Z0-9_.]+)\\}");
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHttpAccessLoggingFilter.class);
	
	private static final ExecutorService httpAccessLogHandlerExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2, new NamedThreadFactory("HTTP-REQUEST-LOGGER"));
	
	private static final String DEFAULT_LOG_TITLE = "用户访问日志";
	
	private static final Map<HttpAccessLogging.LoggingMode,HttpAccessLogDAO> httpAccessLogDAOCacheMap = new HashMap<HttpAccessLogging.LoggingMode,HttpAccessLogDAO>();
	
	private ApplicationContext applicationContext;
	
	private AntPathMatcher pathMatcher = new AntPathMatcher();
	
	private List<MvcResourceMethodMapping> allMvcResourceMethodMappings = new ArrayList<MvcResourceMethodMapping>();
	
	private String defaultLogTitle = DEFAULT_LOG_TITLE;
	
	private boolean loggingRequestHeader = false;
	
	public String getDefaultLogTitle() {
		return defaultLogTitle;
	}

	public void setDefaultLogTitle(String defaultLogTitle) {
		this.defaultLogTitle = defaultLogTitle;
	}

	public boolean isLoggingRequestHeader() {
		return loggingRequestHeader;
	}

	public void setLoggingRequestHeader(boolean loggingRequestHeader) {
		this.loggingRequestHeader = loggingRequestHeader;
	}

	public static ExecutorService getHttpAccessLogHandlerExecutor() {
		return httpAccessLogHandlerExecutor;
	}
	
	public static Map<HttpAccessLogging.LoggingMode, HttpAccessLogDAO> getHttpAccessLogDAOCacheMap() {
		return httpAccessLogDAOCacheMap;
	}

	public AntPathMatcher getPathMatcher() {
		return pathMatcher;
	}

	public void setPathMatcher(AntPathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}
	
	public List<MvcResourceMethodMapping> getAllMvcResourceMethodMappings() {
		return allMvcResourceMethodMappings;
	}

	public void setAllMvcResourceMethodMappings(List<MvcResourceMethodMapping> allMvcResourceMethodMappings) {
		this.allMvcResourceMethodMappings = allMvcResourceMethodMappings;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest requestToUse = request;
		HttpServletResponse responseToUse = response;
		
		HttpAccessLogging httpAccessLogging = null;
		MvcResourceMethodMapping mvcResourceMethodMapping = null;
		if(isRequestSupport(requestToUse)) {
			mvcResourceMethodMapping = getMatchedMvcResourceMethodMapping(requestToUse, responseToUse);
			if(mvcResourceMethodMapping != null) {
				httpAccessLogging = mvcResourceMethodMapping.getResourceMethod().getAnnotation(HttpAccessLogging.class);
			}
		}
		if(httpAccessLogging != null) {
			requestToUse = new ContentCachingRequestWrapper(request);
			responseToUse = new ContentCachingResponseWrapper(response);
			
			HttpAccessLogContext context = new HttpAccessLogContext(mvcResourceMethodMapping, httpAccessLogging, new HttpAccessLog<Object>());
			
			try {
				try {
					beforeAccess(requestToUse, responseToUse, context);
				} catch (Throwable e) {
					LOGGER.error(e.getMessage(), e);
				}
				filterChain.doFilter(requestToUse, responseToUse);
			} finally {
				try {
					afterAccess(requestToUse, responseToUse, context);
				} catch (Throwable e) {
					LOGGER.error(e.getMessage(), e);
				}
				ContentCachingResponseWrapper contentCachedResponse = ServletWebUtils.getContentCachingResponseWrapper(responseToUse);
				if(contentCachedResponse != null){
					contentCachedResponse.copyBodyToResponse(); //重写响应到response.OutputStream中去,否则客户端响应会出现NO CONTENT
				}
			}
		} else {
			filterChain.doFilter(requestToUse, responseToUse);
		}
	}
	
	/**
	 * 请求访问前置拦截处理
	 * @param request
	 * @param response
	 * @param context
	 */
	protected void beforeAccess(HttpServletRequest request, HttpServletResponse response, HttpAccessLogContext context) throws Exception {
		String requestURI = request.getRequestURI();
		HttpAccessLogging httpAccessLogging = context.getHttpAccessLogging();
		HttpAccessLog<?> httpAccessLog = context.getHttpAccessLog();
		httpAccessLog.setTitle(getLogTitle(httpAccessLogging.title()));
		httpAccessLog.setModule(httpAccessLogging.module());
		httpAccessLog.setAccessBeginMillis(System.currentTimeMillis());
		httpAccessLog.setUri(requestURI);
		httpAccessLog.setAccessTime(DateTimeUtils.formatNow());
		Method mvcMethod = context.getMvcResourceMethodMapping().getResourceMethod();
		httpAccessLog.setMvcMethod(mvcMethod.getDeclaringClass().getName() + "." + mvcMethod.getName());
		httpAccessLog.setHttpMethod(request.getMethod());
		httpAccessLog.setClientIpAddr(NetUtils.getRemoteIpAddr(request));
		httpAccessLog.setServerIpAddr(NetUtils.getLocalIpAddr(request));
		httpAccessLog.setRequestContentType(ServletWebUtils.getContentType(request.getContentType()));
		httpAccessLog.setAccessEndMillis(null);
		httpAccessLog.setProcessTime(null);
		httpAccessLog.setAsynRequest(ServletWebUtils.isAjaxRequest(request));
		httpAccessLog.setAppId(GlobalConstants.DEFAULT_APPLICATION_ID);
		if(isLoggingRequestHeader()){
			httpAccessLog.setRequestHeader(extractRequestHeader(request, context));
		}
		httpAccessLog.setAccessUser(getAccessUser(request, context));
	}
	
	/**
	 * 请求访问后置拦截处理
	 * @param request
	 * @param response
	 * @param context
	 */
	protected void afterAccess(HttpServletRequest request, HttpServletResponse response, HttpAccessLogContext context) throws Exception {
		HttpAccessLog<?> httpAccessLog = context.getHttpAccessLog();
		httpAccessLog.setAccessEndMillis(System.currentTimeMillis());
		httpAccessLog.setProcessTime(httpAccessLog.getAccessEndMillis() - httpAccessLog.getAccessBeginMillis());
		httpAccessLog.setLoggingCompleted(true);
		httpAccessLog.setRequestParameter(extractRequestParameter(request, context));
		httpAccessLog.setResponseContentType(ServletWebUtils.getContentType(response.getContentType()));
		httpAccessLog.setResponseResult(extractResponseResult(response, context));
		httpAccessLog.setAccessUser(getAccessUser(request, context));
		executeHttpAccessLoggingTask(request, response, context); //执行日志记录任务
	}
	
	/**
	 * 当前请求是否支持访问日志记录
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean isRequestSupport(HttpServletRequest request) {
		try {
			if(!this.isAsyncDispatch(request) && !this.isAsyncStarted(request)){
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}
	
	/**
	 * 根据请求获取匹配的MvcResourceMethodMapping
	 * @param request
	 * @param response
	 * @return
	 */
	protected MvcResourceMethodMapping getMatchedMvcResourceMethodMapping(HttpServletRequest request, HttpServletResponse response) {
		if(!CollectionUtils.isEmpty(allMvcResourceMethodMappings)) {
			for(MvcResourceMethodMapping mvcResourceMethodMapping : allMvcResourceMethodMappings) {
				List<String> resourceUriPatterns = mvcResourceMethodMapping.getResourceUriPatterns();
				List<String> requestMethods = mvcResourceMethodMapping.getRequestMethods();
				String path = ServletWebUtils.getRequestPath(request, false);
				String method = request.getMethod();
				
				boolean matched = false;
				for(String resourceUriPattern : resourceUriPatterns) {
					if(isPathMatchPattern(resourceUriPattern, path)) { //请求路径匹配
						matched = true;
					}
				}
				matched = matched && (requestMethods.contains("*") || requestMethods.contains(method)); //请求方法匹配
				if(matched) {
					return mvcResourceMethodMapping;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param pattern	- 打在SpringMVC @RequestMapping 或者 Jersey @Path 上的路径pattern
	 * @param path		- 请求URL的相对路径
	 * @return
	 */
	protected boolean isPathMatchPattern(String pattern, String path) {
		try {
			return pathMatcher.match(pattern, path);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return false;
	}
	
	/**
	 * 获取日志标题
	 * @param title
	 * @return
	 */
	protected String getLogTitle(String title) {
		if(!StringUtils.isEmpty(title)){
			Matcher matcher = MESSAGE_SOURCE_CODE_PATTERN.matcher(title);
			if(matcher.find()){
				title = getMessage(matcher.group(1));
			}
		}
		return StringUtils.defaultIfEmpty(title, defaultLogTitle);
	}
	
	/**
	 * 从国际化资源文件中获取message
	 * @param code
	 * @return
	 */
	protected String getMessage(String code) {
		return ApplicationConstants.DEFAULT_MESSAGE_SOURCE_ACCESSOR.get().getMessage(code);
	}
	
	protected String getStringParameterValue(String[] values){
		if(values == null){
			return null;
		}else{
			return values.length == 1 ? values[0] : Arrays.toString(values);
		}
	}
	
	/**
	 * 从HttpServletRequest中提取请求参数(包括请求体中的参数)
	 * @param request
	 * @param context
	 * @return
	 * @throws Exception
	 */
	protected HttpRequestParameter extractRequestParameter(HttpServletRequest request, HttpAccessLogContext context) throws IOException {
		HttpRequestParameter parameter = new HttpRequestParameter();
		Map<String,String[]> originalParamMap = request.getParameterMap();
		Map<String,String> paramMap = new HashMap<String,String>();
		if(originalParamMap != null && !originalParamMap.isEmpty()){
			for(Map.Entry<String, String[]> entry : originalParamMap.entrySet()){
				paramMap.put(entry.getKey(), getStringParameterValue(entry.getValue()));
			}
		}
		parameter.setParameter(paramMap);
		MediaType contentType = context.getHttpAccessLog().getRequestContentType();
		if(contentType != null){
			ContentCachingRequestWrapper requestToUse = ServletWebUtils.getContentCachingRequestWrapper(request);
			if(requestToUse != null) {
				if(contentType.includes(MediaType.APPLICATION_JSON)) { // JSON类型的报文
					String charset = ServletWebUtils.getCharacterEncoding(request, GlobalConstants.DEFAULT_CHARSET);
					byte[] bytes = requestToUse.getContentAsByteArray();
					if(!ArrayUtils.isEmpty(bytes)){
						String bodyStr = new String(bytes, charset);
						Object bodyObj = bodyStr;
						if(JsonUtils.isJsonArray(bodyStr)){ //JSON Array String -> List<Map<String,Object>>
							bodyObj = JsonUtils.json2Object(bodyStr, new TypeReference<List<Map<String,Object>>>(){});
						}else if(JsonUtils.isJsonObject(bodyStr)){ //JSON Object String -> Map<String,Object>
							bodyObj = JsonUtils.json2Object(bodyStr, new TypeReference<Map<String,Object>>(){});
						}
						parameter.setBody(bodyObj);
					}
				} else if(contentType.includes(MediaType.APPLICATION_XML)) { // XML类型的报文
					String charset = ServletWebUtils.getCharacterEncoding(request, GlobalConstants.DEFAULT_CHARSET);
					byte[] bytes = requestToUse.getContentAsByteArray();
					if(!ArrayUtils.isEmpty(bytes)){
						String bodyStr = new String(bytes, charset);
						parameter.setBody(bodyStr);
					}
				} else {
					LOGGER.warn(">>> Found unsupported Content-Type({}) request and ignored!", contentType);
				}
			}
		}
		return excludeRequestParameter(parameter, context);
	}
	
	/**
	 * 记录请求参数时剔除一些敏感数据,如用户密码明文
	 * @param parameter
	 * @param context
	 * @return
	 */
	protected HttpRequestParameter excludeRequestParameter(HttpRequestParameter parameter, HttpAccessLogContext context) {
		HttpAccessLogging httpAccessLogging = context.getHttpAccessLogging();
		String[] excludeNameParams = httpAccessLogging.excludeParams();
		if(excludeNameParams != null && excludeNameParams.length > 0){
			try {
				for(String paramName : excludeNameParams){
					if(parameter.getParameter() != null){
						parameter.getParameter().remove(paramName);
					}
					MediaType contentType = context.getHttpAccessLog().getRequestContentType();
					if (contentType != null && parameter.getBody() != null) {
						if(parameter.getBody() instanceof List){ //JSON Array
							for(Map<String,Object> item : (List<Map<String,Object>>)parameter.getBody()){
								excludeParameter(item, paramName);
							}
						}else if(parameter.getBody() instanceof Map){ //JSON Object
							excludeParameter((Map<String,Object>)parameter.getBody(), paramName);
						}
					}
				}
			} catch (Exception e) {
				logger.error(">>> 排除参数出错! " + e.getMessage());
			}
		}
		return parameter;
	}
	
	/**
	 * 递归剔除参数
	 * @param parameter
	 * @param paramName
	 */
	protected void excludeParameter(Map<String,Object> parameter, String paramName) {
		if(!CollectionUtils.isEmpty(parameter)){
			parameter.remove(paramName);
			for(Map.Entry<String,Object> entry : parameter.entrySet()){
				Object value = entry.getValue();
				if(value != null){
					if(value instanceof List){
						List<Map<String,Object>> list = (List<Map<String, Object>>) value;
						for(Map<String,Object> item : list){
							excludeParameter(item, paramName);
						}
					}else if(value instanceof Map){
						Map<String,Object> item = (Map<String, Object>) value;
						excludeParameter(item, paramName);
					}
				}
			}
		}
	}
	
	/**
	 * 从HttpServletRequest中提取请求头信息
	 * @param request
	 * @param context
	 * @return
	 */
	protected Map<String,String> extractRequestHeader(HttpServletRequest request, HttpAccessLogContext context) {
		Map<String,String> headerMap = new HashMap<String,String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		if(headerNames != null){
			while(headerNames.hasMoreElements()){
				String headerName = headerNames.nextElement();
				headerMap.put(headerName, request.getHeader(headerName));
			}
		}
		return headerMap;
	}
	
	/**
	 * 提取请求结果
	 * @param request
	 * @param context
	 * @return
	 */
	protected Object extractResponseResult(HttpServletResponse response, HttpAccessLogContext context) throws IOException {
		MediaType contentType = context.getHttpAccessLog().getResponseContentType();
		if(contentType != null){
			ContentCachingResponseWrapper responseToUse = ServletWebUtils.getContentCachingResponseWrapper(response);
			if(responseToUse != null){
				if(contentType.includes(MediaType.APPLICATION_JSON) || contentType.includes(MediaType.APPLICATION_XML)) { // JSON/XML类型的报文
					String charset = ServletWebUtils.getCharacterEncoding(response, GlobalConstants.DEFAULT_CHARSET);
					byte[] bytes = responseToUse.getContentAsByteArray();
					if(bytes != null){
						return new String(bytes, charset);
					}
				} else {
					LOGGER.warn(">>> Found unsupport Content-Type({}) response and ignored!");
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取日志DAO
	 * @param context
	 * @return
	 */
	protected HttpAccessLogDAO getHttpAccessLogDAO(HttpAccessLogContext context) {
		HttpAccessLogDAO httpAccessLogDAO = httpAccessLogDAOCacheMap.get(context.getHttpAccessLogging().loggingMode());
		Assert.state(httpAccessLogDAO != null, "No bean found of type : " + HttpAccessLogDAO.class.getName());
		return httpAccessLogDAO;
	}
	
	/**
	 * 执行日志记录任务
	 * @param request
	 * @param response
	 * @param context
	 */
	protected void executeHttpAccessLoggingTask(HttpServletRequest request, HttpServletResponse response, HttpAccessLogContext context) {
		HttpAccessLogDAO httpAccessLogDAO = getHttpAccessLogDAO(context);
		if(httpAccessLogDAO != null){
			getHttpAccessLogHandlerExecutor().submit(new DefaultHttpAccessLoggingTask(request, response, context, httpAccessLogDAO));
		}
	}
	
	@Override
	public void afterPropertiesSet() throws ServletException {
		//nothing to do, to avoid initFilterBean() method invoke twice
		initAllMvcResourceMethodMappings();
		initHttpAccessLogDAOCacheMap();
	}
	
	public void destroy() {
		getHttpAccessLogHandlerExecutor().shutdown();
		getHttpAccessLogDAOCacheMap().clear();
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * 根据项目中所有的SpringMVC/Jersey控制方法来初始化所有MvcResourceMethodMapping
	 */
	protected abstract void initAllMvcResourceMethodMappings();
	
	/**
	 * 初始化HttpAccessLogDAO的缓存cache
	 */
	protected void initHttpAccessLogDAOCacheMap() {
		Map<String, HttpAccessLogDAO> beans = applicationContext.getBeansOfType(HttpAccessLogDAO.class);
		if(!CollectionUtils.isEmpty(beans)) {
			for(Map.Entry<String, HttpAccessLogDAO> entry : beans.entrySet()) {
				HttpAccessLogDAO httpAccessLogDAO = entry.getValue();
				getHttpAccessLogDAOCacheMap().put(httpAccessLogDAO.getLoggingMode(), httpAccessLogDAO);
			}
		}
	}
	
	/**
	 * 获取操作人的LoginUser对象
	 * @param request
	 * @param context
	 * @return
	 */
	protected abstract <T> T getAccessUser(HttpServletRequest request, HttpAccessLogContext context);
	
	
	@SuppressWarnings("unused")
	public class DefaultHttpAccessLoggingTask implements Runnable {

		private final HttpServletRequest request;
		
		private final HttpServletResponse response;
		
		private final HttpAccessLogContext context;
		
		private final HttpAccessLogDAO httpAccessLogDAO;
		
		public DefaultHttpAccessLoggingTask(HttpServletRequest request, HttpServletResponse response, HttpAccessLogContext context, HttpAccessLogDAO httpAccessLogDAO) {
			super();
			this.request = request;
			this.response = response;
			this.context = context;
			this.httpAccessLogDAO = httpAccessLogDAO;
		}

		public void run() {
			try {
				HttpAccessLog<?> httpAccessLog = context.getHttpAccessLog();
				if (httpAccessLog != null) {
					logger.debug(">>> Http access log : " + httpAccessLog);
					httpAccessLogDAO.saveLog((HttpAccessLog<?>) httpAccessLog);
				} 
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			
		}
		
	}
	
}
