package com.penglecode.xmodule.springboot.examples.validator.support;

import java.util.Arrays;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.beanvalidation.MethodValidationInterceptor;

/**
 * 自定义的MethodValidationInterceptor
 * 用以解决只能在类级别上应用@Validated注解中使用groups组，而在方法上指定groups组时无效的问题
 * 
 * @author pengpeng
 * @date 2020年1月20日 上午10:20:46
 */
public class CustomMethodValidationInterceptor extends MethodValidationInterceptor {

	@Override
	protected Class<?>[] determineValidationGroups(MethodInvocation invocation) {
		final Class<?>[] classLevelGroups = super.determineValidationGroups(invocation);
		final ValidatedGroups validatedGroups = AnnotationUtils.findAnnotation(invocation.getMethod(), ValidatedGroups.class);
		final Class<?>[] methodLevelGroups = validatedGroups != null ? validatedGroups.value() : new Class<?>[0];
		if (methodLevelGroups.length == 0) {
			return classLevelGroups;
		}

		final int newLength = classLevelGroups.length + methodLevelGroups.length;
		final Class<?>[] mergedGroups = Arrays.copyOf(classLevelGroups, newLength);
		System.arraycopy(methodLevelGroups, 0, mergedGroups, classLevelGroups.length, methodLevelGroups.length);

		return mergedGroups;
	}

}
