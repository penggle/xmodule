package com.penglecode.xmodule.common.util;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Stream;

/**
 * 基于Spring类路径上类或接口的扫描工具类
 * 
 * @author 	pengpeng
 * @date 	2020年1月18日 下午1:12:51
 */
public class ClassScanningUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassScanningUtils.class);
	
	/**
	 * 根据多个包名搜索class的类名
	 * 
	 * @param basePackages 包名(可以有通配符)
	 * @return 类名的集合
	 */
	public static Set<String> scanClassNames(Class<?>... basePackages) {
		Assert.notEmpty(basePackages, "Parameter 'basePackages' can not be empty!");
		return scanClassNames(Stream.of(basePackages).map(pkg -> pkg.getPackage().getName()).toArray(String[]::new));
	}
	
	/**
	 * 根据多个包名搜索class的类名
	 * 
	 * @param basePackages 搜索包名(可以有通配符)
	 * @return 类名的集合
	 */
	public static Set<String> scanClassNames(String... basePackages) {
		Assert.notEmpty(basePackages, "Parameter 'basePackages' can not be empty!");
		ClassPathScanningCandidateComponentProvider scanningProvider = new ClassPathScanningAnyTypeProvider();
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
	 * 获取指定类型的所有子类型(包括自己、子接口等)
	 * @param superType
	 * @param basePackages
	 * @return
	 */
	public static Set<String> scanClassNames(Class<?> superType, Class<?>... basePackages) {
		Assert.notNull(superType, "Parameter 'superType' can not be empty!");
		Assert.notEmpty(basePackages, "Parameter 'basePackages' can not be empty!");
		return scanClassNames(superType, Stream.of(basePackages).map(pkg -> pkg.getPackage().getName()).toArray(String[]::new));
	}

	/**
	 * 获取指定类型的所有子类型(包括自己、子接口等)
	 * @param superType
	 * @param basePackages
	 * @return
	 */
	public static Set<String> scanClassNames(Class<?> superType, String... basePackages) {
		Assert.notNull(superType, "Parameter 'superType' can not be empty!");
		Assert.notEmpty(basePackages, "Parameter 'basePackages' can not be empty!");
		ClassPathScanningCandidateComponentProvider scanningProvider = new ClassPathScanningCandidateTypeProvider(true, false);
		scanningProvider.addIncludeFilter(new AssignableTypeFilter(superType));
		Set<String> classNames = new LinkedHashSet<>();
		for(String basePackage : basePackages) {
			Set<BeanDefinition> candidateBeanDefinitions = scanningProvider.findCandidateComponents(basePackage);
			for(BeanDefinition candidateBeanDefinition : candidateBeanDefinitions) {
				classNames.add(candidateBeanDefinition.getBeanClassName());
			}
		}
		return classNames;
	}

	/**
	 * 根据多个包名搜索具有指定注解的类或接口
	 * (注意：该方法忽略java.lang.NoClassDefFoundError错误)
	 *
	 * @param classAnnotation	要搜索的类的具有的注解
	 * @return 类的集合
	 */
	public static Set<String> scanAnnotatedClassNames(Class<? extends Annotation> classAnnotation, Class<?>... basePackages) {
		Assert.isTrue(classAnnotation != null, "Parameter 'classAnnotation' must be required at least one!");
		Assert.notEmpty(basePackages, "Parameter 'basePackages' can not be empty!");
		return scanAnnotatedClassNames(classAnnotation, Stream.of(basePackages).map(pkg -> pkg.getPackage().getName()).toArray(String[]::new));
	}

	/**
	 * 根据多个包名搜索具有指定注解的类或接口
	 * (注意：该方法忽略java.lang.NoClassDefFoundError错误)
	 *
	 * @param classAnnotation	要搜索的类的具有的注解
	 * @return 类的集合
	 */
	public static Set<String> scanAnnotatedClassNames(Class<? extends Annotation> classAnnotation, String... basePackages) {
		Assert.isTrue(classAnnotation != null, "Parameter 'classAnnotation' must be required at least one!");
		Assert.notEmpty(basePackages, "Parameter 'basePackages' can not be empty!");
		ClassPathScanningCandidateComponentProvider scanningProvider = new ClassPathScanningCandidateTypeProvider(true, false);
		scanningProvider.addIncludeFilter(new AnnotationTypeFilter(classAnnotation));
		Set<String> classNames = new LinkedHashSet<>();
		for(String basePackage : basePackages) {
			Set<BeanDefinition> candidateBeanDefinitions = scanningProvider.findCandidateComponents(basePackage);
			for(BeanDefinition candidateBeanDefinition : candidateBeanDefinitions) {
				classNames.add(candidateBeanDefinition.getBeanClassName());
			}
		}
		return classNames;
	}
	
	/**
	 * 可以扫描任何类的ClassPathScanningCandidateComponentProvider
	 */
	public static class ClassPathScanningAnyTypeProvider extends ClassPathScanningCandidateComponentProvider {
		
		public ClassPathScanningAnyTypeProvider() {
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
	public static class ClassPathScanningCandidateTypeProvider extends ClassPathScanningCandidateComponentProvider {

		/**
		 * 扫描结果包含所有类型，包括：接口、抽象类等?
		 */
		private final boolean considerAllTypes;

		public ClassPathScanningCandidateTypeProvider(boolean useDefaultFilters) {
			this(false, useDefaultFilters);
		}

		public ClassPathScanningCandidateTypeProvider(boolean considerAllTypes, boolean useDefaultFilters) {
			super(useDefaultFilters);
			this.considerAllTypes = considerAllTypes;
		}

		public ClassPathScanningCandidateTypeProvider(boolean considerAllTypes, boolean useDefaultFilters, Environment environment) {
			super(useDefaultFilters, environment);
			this.considerAllTypes = considerAllTypes;
		}

		@Override
		protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
			if(considerAllTypes) {
				return true;
			}
			return super.isCandidateComponent(beanDefinition);
		}
	}

	/**
	 * 解决根据包名扫描类时扫描不到rt.jar包下的类
	 *
	 * 注本类只适用于JDK8及以下
	 */
	public static class JavaCoreResourceClassLoader extends ClassLoader {

		public JavaCoreResourceClassLoader() {
			super();
		}

		public JavaCoreResourceClassLoader(ClassLoader parent) {
			super(parent);
		}

		@Override
		protected URL findResource(String name) {
			if(isJavaCorePackageResource(name)) {
				URL url = getJavaCorePackageResource(name);
				if(url != null) {
					return url;
				}
			}
			return super.findResource(name);
		}

		@Override
		protected Enumeration<URL> findResources(String name) throws IOException {
			if(isJavaCorePackageResource(name)) {
				URL url = getJavaCorePackageResource(name);
				if(url != null) {
					Vector<URL> urls = new Vector<>();
					urls.add(url);
					return urls.elements();
				}
			}
			return super.findResources(name);
		}

		/**
		 * 判断name是不是package式的资源表示形式，例如：java/、java/util等
		 * @param name
		 * @return
		 */
		protected boolean isJavaCorePackageResource(String name) {
			return !StringUtils.isEmpty(name) && name.startsWith("java/") && !name.endsWith(".class");
		}

		/**
		 * java核心包名，例如：java、java/util等
		 * @param name
		 * @return
		 */
		protected URL getJavaCorePackageResource(String name) {
			try {
				String sample = "java/lang/Object.class";
				URL sampleUrl = getResource(sample);
				String stringUrl = sampleUrl.toString();
				stringUrl = stringUrl.substring(0, stringUrl.indexOf(sample));
				return new URL(stringUrl);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
}
