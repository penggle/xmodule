package com.penglecode.xmodule.common.web.servlet.filter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;

import com.penglecode.xmodule.common.util.ArrayUtils;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.SpringUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.support.HttpAccessLogging;
import com.penglecode.xmodule.common.web.support.MvcResourceMethodMapping;

/**
 * 基于SpringMVC框架的HttpAccessLoggingFilter基类
 * 
 * @author 	pengpeng
 * @date	2019年7月27日 下午4:47:39
 */
public abstract class AbstractSpringMvcHttpAccessLoggingFilter extends AbstractHttpAccessLoggingFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSpringMvcHttpAccessLoggingFilter.class);
	
	@Override
	protected void initAllMvcResourceMethodMappings() {
		AbstractHandlerMethodMapping<?> springMvcResourceMethodMapping = SpringUtils.getBean(AbstractHandlerMethodMapping.class);
		Map<?, HandlerMethod> handlerMethods = springMvcResourceMethodMapping.getHandlerMethods();
		if(!CollectionUtils.isEmpty(handlerMethods)) {
			List<MvcResourceMethodMapping> allMvcResourceMethodMappings = new ArrayList<MvcResourceMethodMapping>();
			for(Map.Entry<?, HandlerMethod> entry : handlerMethods.entrySet()) {
				HandlerMethod handlerMethod = entry.getValue();
				Method resourceMethod = handlerMethod.getMethod();
				if(AnnotationUtils.findAnnotation(resourceMethod, HttpAccessLogging.class) != null) {
					Class<?> resourceClass = handlerMethod.getBeanType();
					RequestMapping classRequestMapping = AnnotatedElementUtils.findMergedAnnotation(resourceClass, RequestMapping.class);
					RequestMapping methodRequestMapping = AnnotatedElementUtils.findMergedAnnotation(resourceMethod, RequestMapping.class);
					MvcResourceMethodMapping mvcResourceMethodMapping = resolveMvcResourceMethodMapping(resourceClass, resourceMethod, classRequestMapping, methodRequestMapping);
					if(mvcResourceMethodMapping != null) {
						allMvcResourceMethodMappings.add(mvcResourceMethodMapping);
						LOGGER.info("Add {}", mvcResourceMethodMapping);
					}
				}
			}
			setAllMvcResourceMethodMappings(allMvcResourceMethodMappings);
		}
	}
	
	protected MvcResourceMethodMapping resolveMvcResourceMethodMapping(Class<?> resourceClass, Method resourceMethod, RequestMapping classRequestMapping, RequestMapping methodRequestMapping) {
		String backupMethod = "*";
		String[] mainMethods = null;
		String[] uriPrefixs = new String[] {""}, uriSuffixs = new String[] {""};
		if(classRequestMapping != null) {
			if(!ArrayUtils.isEmpty(classRequestMapping.value())) {
				uriPrefixs = classRequestMapping.value();
			}
			if(!ArrayUtils.isEmpty(classRequestMapping.method())) {
				backupMethod = classRequestMapping.method()[0].toString();
			}
		}
		if(methodRequestMapping != null) {
			if(!ArrayUtils.isEmpty(methodRequestMapping.value())) {
				uriSuffixs = methodRequestMapping.value();
			}
			RequestMethod[] methods = methodRequestMapping.method();
			List<String> methodList = new ArrayList<String>();
			for(RequestMethod method : methods) {
				methodList.add(method.toString());
			}
			mainMethods = methodList.toArray(new String[0]);
			if(ArrayUtils.isEmpty(mainMethods)) {
				mainMethods = new String[] {backupMethod};
			}
			List<String> resourceUriPatterns = new ArrayList<String>();
			for(String uriPrefix : uriPrefixs) {
				for(String uriSuffix : uriSuffixs) {
					String restUri = uriPrefix;
					if(!uriPrefix.endsWith("/") && !uriSuffix.startsWith("/")) {
						restUri += "/";
					}
					restUri += uriSuffix;
					if(!restUri.startsWith("/")) {
						restUri = "/" + restUri;
					}
					String resourceUriPattern = StringUtils.stripEnd(restUri, "/");
					resourceUriPatterns.add(resourceUriPattern);
				}
			}
			return new MvcResourceMethodMapping(resourceClass, resourceMethod, Arrays.asList(mainMethods), resourceUriPatterns);
		}
		return null;
	}
	
}
