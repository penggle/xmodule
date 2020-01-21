package com.penglecode.xmodule.spring.examples.core.annotation;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

public class AnnotatedElementUtilsExample {

	public static void findMergedAnnotation() {
		Method method = ReflectionUtils.findMethod(UserApiService.class, "createUser", new Class<?>[] {Map.class});
		RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
		System.out.println(requestMapping);
	}
	
	public static void main(String[] args) {
		findMergedAnnotation();
	}

}
