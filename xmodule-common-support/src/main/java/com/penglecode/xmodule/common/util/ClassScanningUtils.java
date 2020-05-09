package com.penglecode.xmodule.common.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.consts.ApplicationConstants;

/**
 * 基于Spring类路径上类或接口的扫描工具类
 * 
 * @author 	pengpeng
 * @date 	2020年1月18日 下午1:12:51
 */
@SuppressWarnings("unchecked")
public class ClassScanningUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassScanningUtils.class);
	
	/**
	 * 根据多个包名搜索class的类名
	 * 
	 * @param basePackages 包名(可以有通配符)
	 * @return 类名的集合
	 */
	public static Set<String> scanPackageClassNames(Class<?>... basePackages) {
		Assert.notEmpty(basePackages, "Parameter 'basePackages' can not be empty!");
		return scanPackageClassNames(Stream.of(basePackages).map(pkg -> pkg.getPackage().getName()).toArray(l -> new String[l]));
	}
	
	/**
	 * 根据多个包名搜索class的类名
	 * 
	 * @param basePackages 搜索包名(可以有通配符)
	 * @return 类名的集合
	 */
	public static Set<String> scanPackageClassNames(String... basePackages) {
		Assert.notEmpty(basePackages, "Parameter 'basePackages' can not be empty!");
		ClassPathScanningCandidateComponentProvider scanningProvider = new ClassPathScanningAnyClassProvider();
		Set<String> classNames = new LinkedHashSet<String>();
		for(String basePackage : basePackages) {
			Set<BeanDefinition> candidateBeanDefinitions = scanningProvider.findCandidateComponents(basePackage);
			for(BeanDefinition candidateBeanDefinition : candidateBeanDefinitions) {
				classNames.add(candidateBeanDefinition.getBeanClassName());
			}
		}
		return classNames;
	}
	
	/**
	 * 根据多个包名搜索指定类型的类或接口(不包括自身)
	 * (注意：该方法忽略java.lang.NoClassDefFoundError错误)
	 * 
	 * @param superType		要搜索的类的超类
	 * @param basePackages 搜索包名Class
	 * @return 类的集合
	 */
	public static <T> Set<Class<? extends T>> scanPackageClasses(Class<? extends T> superType, Class<?>... basePackages) {
		Assert.notNull(superType, "Parameter 'superType' can not be empty!");
		Assert.notEmpty(basePackages, "Parameter 'basePackages' can not be empty!");
		return scanPackageClasses(superType, Stream.of(basePackages).map(pkg -> pkg.getPackage().getName()).toArray(l -> new String[l]));
	}
	
	/**
	 * 根据多个包名搜索指定类型的类或接口(不包括自身)
	 * (注意：该方法忽略java.lang.NoClassDefFoundError错误)
	 * 
	 * @param superType		要搜索的类的超类
	 * @param basePackages 搜索	包名(可以有通配符)
	 * @return 类的集合
	 */
	public static <T> Set<Class<? extends T>> scanPackageClasses(Class<? extends T> superType, String... basePackages) {
		Assert.notNull(superType, "Parameter 'superType' can not be empty!");
		Assert.notEmpty(basePackages, "Parameter 'basePackages' can not be empty!");
		ClassPathScanningCandidateComponentProvider scanningProvider = new ClassPathScanningCandidateClassProvider();
		scanningProvider.addIncludeFilter((mr, mrf) -> {
			Class<?> clazz = classForName(mr.getClassMetadata().getClassName());
			return clazz != null && !superType.equals(clazz) && superType.isAssignableFrom(clazz);
		});
		Set<Class<? extends T>> classes = new LinkedHashSet<Class<? extends T>>();
		for(String basePackage : basePackages) {
			Set<BeanDefinition> candidateBeanDefinitions = scanningProvider.findCandidateComponents(basePackage);
			for(BeanDefinition candidateBeanDefinition : candidateBeanDefinitions) {
				classes.add((Class<? extends T>) ClassUtils.forName(candidateBeanDefinition.getBeanClassName()));
			}
		}
		return classes;
	}
	
	/**
	 * 根据多个包名搜索具有指定注解的类或接口
	 * (注意：该方法忽略java.lang.NoClassDefFoundError错误)
	 * 
	 * @param classAnnotation	要搜索的类的具有的注解
	 * @param methodAnnotation 要搜索的类的方法上具有的注解
	 * @param basePackages 		搜索包名Class
	 * @return 类的集合
	 */
	public static Set<Class<?>> scanPackageAnnotatedClasses(Class<? extends Annotation> classAnnotation, Class<? extends Annotation> methodAnnotation, Class<?>... basePackages) {
		Assert.isTrue(classAnnotation != null || methodAnnotation != null, "Parameter 'classAnnotation' or 'methodAnnotation' must be required at least one!");
		Assert.notEmpty(basePackages, "Parameter 'basePackages' can not be empty!");
		return scanPackageAnnotatedClasses(classAnnotation, methodAnnotation, Stream.of(basePackages).map(pkg -> pkg.getPackage().getName()).toArray(l -> new String[l]));
	}
	
	/**
	 * 根据多个包名搜索具有指定注解的类或接口
	 * (注意：该方法忽略java.lang.NoClassDefFoundError错误)
	 * 
	 * @param classAnnotation	要搜索的类的具有的注解
	 * @param methodAnnotation 要搜索的类的方法上具有的注解
	 * @param basePackages 		搜索包名(可以有通配符)
	 * @return 类的集合
	 */
	public static Set<Class<?>> scanPackageAnnotatedClasses(Class<? extends Annotation> classAnnotation, Class<? extends Annotation> methodAnnotation, String... basePackages) {
		Assert.isTrue(classAnnotation != null || methodAnnotation != null, "Parameter 'classAnnotation' or 'methodAnnotation' must be required at least one!");
		Assert.notEmpty(basePackages, "Parameter 'basePackages' can not be empty!");
		ClassPathScanningCandidateComponentProvider scanningProvider = new ClassPathScanningCandidateClassProvider();
		scanningProvider.addIncludeFilter((mr, mrf) -> {
			AnnotationMetadata am = mr.getAnnotationMetadata();
			boolean candidate = false;
			if(classAnnotation != null) {
				candidate = am.hasAnnotation(classAnnotation.getName()) || candidate;
			}
			if(methodAnnotation != null) {
				candidate = am.hasAnnotatedMethods(methodAnnotation.getName()) || candidate;
			}
			return candidate && classForName(mr.getClassMetadata().getClassName()) != null;
		});
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		for(String basePackage : basePackages) {
			Set<BeanDefinition> candidateBeanDefinitions = scanningProvider.findCandidateComponents(basePackage);
			for(BeanDefinition candidateBeanDefinition : candidateBeanDefinitions) {
				classes.add((Class<?>) ClassUtils.forName(candidateBeanDefinition.getBeanClassName()));
			}
		}
		return classes;
	}
	
	protected static Class<?> classForName(String className) {
		Class<?> clazz = null;
		try {
			clazz = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
		} catch (Throwable e) {
			LOGGER.warn("Class scanning error: {} - {}", e.getClass().getName(), e.getMessage());
		}
		return clazz;
	}
	
	/**
	 * 可以扫描任何类的ClassPathScanningCandidateComponentProvider
	 */
	public static class ClassPathScanningAnyClassProvider extends ClassPathScanningCandidateComponentProvider {
		
		public ClassPathScanningAnyClassProvider() {
			super(false);
			Environment environment = ApplicationConstants.ENVIRONMENT.get();
			if(environment != null) {
				setEnvironment(environment);
			}
		}

		@Override
		protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
			return true;
		}
		
		@Override
		protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
			return true;
		}
		
	}
	
	/**
	 * 可扫描指定类的ClassPathScanningCandidateComponentProvider
	 */
	public static class ClassPathScanningCandidateClassProvider extends ClassPathScanningCandidateComponentProvider {
		
		public ClassPathScanningCandidateClassProvider() {
			super(false);
			Environment environment = ApplicationConstants.ENVIRONMENT.get();
			if(environment != null) {
				setEnvironment(environment);
			}
		}
		
		@Override
		protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
			return true;
		}
		
	}
	
}
