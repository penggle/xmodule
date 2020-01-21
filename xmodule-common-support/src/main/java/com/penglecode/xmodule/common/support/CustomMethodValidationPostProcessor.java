package com.penglecode.xmodule.common.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.MethodValidationInterceptor;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@SuppressWarnings("serial")
public class CustomMethodValidationPostProcessor extends MethodValidationPostProcessor {

	protected Advice createMethodValidationAdvice(@Nullable Validator validator) {
		return (validator != null ? new CustomMethodValidationInterceptor(validator) : new CustomMethodValidationInterceptor());
	}
	
	public static class CustomMethodValidationInterceptor extends MethodValidationInterceptor {

		private static final Logger LOGGER = LoggerFactory.getLogger(CustomMethodValidationInterceptor.class);
		
		public CustomMethodValidationInterceptor() {
			super();
		}

		public CustomMethodValidationInterceptor(Validator validator) {
			super(validator);
		}

		public CustomMethodValidationInterceptor(ValidatorFactory validatorFactory) {
			super(validatorFactory);
		}
		
		protected Class<?>[] determineValidationGroups(MethodInvocation invocation) {
			Method method = invocation.getMethod();
			List<Validated> validatedAnnoList = new ArrayList<Validated>();
			//提取方法参数上的@Validated注解
			Stream.of(method.getAnnotatedParameterTypes()).forEach(at -> {
				Optional.ofNullable(AnnotationUtils.getAnnotation(at, Validated.class)).ifPresent(validatedAnnoList::add);
			});
			//提取方法上的@Validated注解
			Optional.ofNullable(AnnotationUtils.findAnnotation(method, Validated.class)).ifPresent(validatedAnnoList::add);
			
			//如果存在接口与实现类或者继承的情况
			if(!method.getDeclaringClass().equals(invocation.getThis().getClass())) { //interface and implementation
				try {
					method = invocation.getThis().getClass().getMethod(method.getName(), method.getParameterTypes());
					//提取方法参数上的@Validated注解
					Stream.of(method.getAnnotatedParameterTypes()).forEach(at -> {
						Optional.ofNullable(AnnotationUtils.getAnnotation(at, Validated.class)).ifPresent(validatedAnnoList::add);
					});
					//提取方法上的@Validated注解
					Optional.ofNullable(AnnotationUtils.findAnnotation(method, Validated.class)).ifPresent(validatedAnnoList::add);
				} catch (NoSuchMethodException e) {
					LOGGER.error(e.getMessage());
				}
			}
			
			List<Class<?>> groupClasses = validatedAnnoList.stream().flatMap(v -> Stream.of(v.value())).distinct().collect(Collectors.toList());
			if(!groupClasses.contains(Default.class)) {
				groupClasses.add(Default.class);
			}
			return groupClasses.toArray(new Class<?>[0]);
		}
		
	}
	
}
