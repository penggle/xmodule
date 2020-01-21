package com.penglecode.xmodule.common.web.springmvc.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.penglecode.xmodule.common.support.DtoModel;
import com.penglecode.xmodule.common.util.ReflectionUtils;

public class EnhancedRequestMappingHandlerAdapter extends RequestMappingHandlerAdapter {

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>(getArgumentResolvers());
		replaceRequestParamMethodArgumentResolvers(argumentResolvers);
		setArgumentResolvers(argumentResolvers);
		
		List<HandlerMethodArgumentResolver> initBinderArgumentResolvers = new ArrayList<HandlerMethodArgumentResolver>(getInitBinderArgumentResolvers());
		replaceRequestParamMethodArgumentResolvers(initBinderArgumentResolvers);
		setInitBinderArgumentResolvers(initBinderArgumentResolvers);
	}
	
	/**
	 * 替换RequestParamMethodArgumentResolver为增强版的EnhancedRequestParamMethodArgumentResolver
	 * @param methodArgumentResolvers
	 */
	protected void replaceRequestParamMethodArgumentResolvers(List<HandlerMethodArgumentResolver> methodArgumentResolvers) {
		methodArgumentResolvers.forEach(argumentResolver -> {
			if(argumentResolver.getClass().equals(RequestParamMethodArgumentResolver.class)) {
				Boolean useDefaultResolution = ReflectionUtils.getFieldValue(argumentResolver, "useDefaultResolution");
				EnhancedRequestParamMethodArgumentResolver enhancedArgumentResolver = new EnhancedRequestParamMethodArgumentResolver(getBeanFactory(), useDefaultResolution);
				enhancedArgumentResolver.setResolvableParameterTypes(Arrays.asList(DtoModel.class));
				Collections.replaceAll(methodArgumentResolvers, argumentResolver, enhancedArgumentResolver);
			}
		});
	}
	
}
