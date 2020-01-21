package com.penglecode.xmodule.spring.examples.core.annotation;

import java.util.Map;
import java.util.Set;

import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.web.bind.annotation.RequestMapping;

public class AnnotationMetadataExample {

	/**
	 * 获取指定Class上的所有注解类型(name)
	 */
	public static void testGetAnnotationTypes() {
		AnnotationMetadata annoMetadata = AnnotationMetadata.introspect(UserApiService.class);
		System.out.println(annoMetadata);
		Set<String> annotations = annoMetadata.getAnnotationTypes(); //获取类上的注解类型名
		annotations.stream().forEach(System.out::println);
	}
	
	/**
	 * 获取指定Class上的所有方法上的注解
	 */
	public static void testGetAnnotatedMethods() {
		AnnotationMetadata annoMetadata = AnnotationMetadata.introspect(UserApiService.class);
		System.out.println(annoMetadata);
		String annotationName = null;
		
		annotationName = RequestMapping.class.getName();
		Set<MethodMetadata> annotatedMethods = annoMetadata.getAnnotatedMethods(annotationName);
		if(annotatedMethods != null) {
			annotatedMethods.stream().forEach(method -> {
				String fullMethodName = method.getDeclaringClassName() + "." + method.getMethodName();
				System.out.println(String.format(">>> %s(...) merged annotations :", fullMethodName));
				MergedAnnotations annotations = method.getAnnotations();
				annotations.forEach(anno -> {
					System.out.println(anno.getType() + " = " + anno);
				});
				
				MergedAnnotation<RequestMapping> mergedAnnotation = annotations.get(RequestMapping.class);
				System.out.println(mergedAnnotation);
			});
		}
	}
	
	/**
	 * 获取指定Class上的注解属性
	 */
	public static void testGetAnnotationAttributes() {
		AnnotationMetadata annoMetadata = AnnotationMetadata.introspect(UserApiService.class);
		System.out.println(annoMetadata);
		String annotationName = null;
		
		annotationName = RequestMapping.class.getName();
		//获取#UserApiService上的#RequestMapping注解属性
		Map<String,Object> annoAttributes = annoMetadata.getAnnotationAttributes(annotationName);
		System.out.println(annoAttributes);
	}
	
	public static void main(String[] args) {
		testGetAnnotationTypes();
		//testGetAnnotatedMethods();
		//testGetAnnotationAttributes();
	}

}
