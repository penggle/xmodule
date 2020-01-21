package com.penglecode.xmodule.spring.examples.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("deprecation")
public class AnnotationUtilsExample {

	/**
	 * 判断某个Class上(类上、方法上、方法参数上)是否合适标注指定的注解
	 */
	public static void isCandidateClass() {
		boolean candidate = false;
		candidate = AnnotationUtils.isCandidateClass(UserApiService.class, SafeVarargs.class);
		System.out.println(String.format(">>> has annotation(@%s) : %s", SafeVarargs.class.getName(), candidate));
		
		candidate = AnnotationUtils.isCandidateClass(UserApiService.class, RequestMapping.class);
		System.out.println(String.format(">>> has annotation(@%s) : %s", RequestMapping.class.getName(), candidate));
		
		candidate = AnnotationUtils.isCandidateClass(UserApiService.class, PostConstruct.class);
		System.out.println(String.format(">>> has annotation(@%s) : %s", PostConstruct.class.getName(), candidate));
		
	}
	
	/**
	 * 获取指定类型上的单个注解(此方法仅获取当前类型的注解，不包括其父类上的注解，如果需要请使用findAnnotation(...)方法)
	 */
	public static void getAnnotation() {
		Mapping mappingAnno = AnnotationUtils.getAnnotation(RequestMapping.class, Mapping.class); //获取注解上的注解
		System.out.println(mappingAnno);
		RequestMapping requestMapping = AnnotationUtils.getAnnotation(UserApiService.class, RequestMapping.class); //获取指定Class上的注解
		System.out.println(requestMapping);
		ResponseStatus responseStatus = AnnotationUtils.getAnnotation(UserApiService.class, ResponseStatus.class); //无法获取父类上的注解
		System.out.println(responseStatus);
	}
	
	/**
	 * 获取指定类型上的多个注解(此方法仅获取当前类型的注解，不包括其父类上的注解，如果需要请使用findAnnotation(...)方法)
	 */
	public static void getAnnotations() {
		Annotation[] annotations = null;
		annotations = AnnotationUtils.getAnnotations(UserApiService.class);
		for(Annotation anno : annotations) {
			System.out.println(anno);
		}
		
		System.out.println("-------------------------------------");
		Method method = ReflectionUtils.findMethod(UserApiService.class, "createUser", new Class<?>[] {Map.class});
		annotations = AnnotationUtils.getAnnotations(method);
		for(Annotation anno : annotations) {
			System.out.println(anno);
		}
	}
	
	/**
	 * 查找指定类型上的指定注解(包括搜索父类)
	 */
	public static void findAnnotation() {
		RequestMapping requestMapping = AnnotationUtils.findAnnotation(UserApiService.class, RequestMapping.class); //获取指定Class上的注解
		System.out.println(requestMapping);
		ResponseStatus responseStatus = AnnotationUtils.findAnnotation(UserApiService.class, ResponseStatus.class); //无法获取父类上的注解
		System.out.println(responseStatus);
	}
	
	public static void main(String[] args) {
		//isCandidateClass();
		//getAnnotation();
		getAnnotations();
		//findAnnotation();
	}

}
