package com.penglecode.xmodule.spring.examples.core.annotation;

import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy;

public class MergedAnnotationsExample {

	public static void findAnnotation() {
		MergedAnnotations annotations = null;
		
		annotations = MergedAnnotations.from(UserApiService.class, SearchStrategy.DIRECT);
		System.out.println(">>> 搜索模式：" + SearchStrategy.DIRECT);
		annotations.forEach(anno -> {
			System.out.println(anno);
		});
		
		annotations = MergedAnnotations.from(UserApiService.class, SearchStrategy.INHERITED_ANNOTATIONS);
		System.out.println(">>> 搜索模式：" + SearchStrategy.INHERITED_ANNOTATIONS);
		annotations.forEach(anno -> {
			System.out.println(anno);
		});
		
		annotations = MergedAnnotations.from(UserApiService.class, SearchStrategy.SUPERCLASS);
		System.out.println(">>> 搜索模式：" + SearchStrategy.SUPERCLASS);
		annotations.forEach(anno -> {
			System.out.println(anno);
		});
		
		annotations = MergedAnnotations.from(UserApiService.class, SearchStrategy.TYPE_HIERARCHY);
		System.out.println(">>> 搜索模式：" + SearchStrategy.TYPE_HIERARCHY);
		annotations.forEach(anno -> {
			System.out.println(anno);
		});
	}
	
	public static void main(String[] args) {
		findAnnotation();
	}

}
