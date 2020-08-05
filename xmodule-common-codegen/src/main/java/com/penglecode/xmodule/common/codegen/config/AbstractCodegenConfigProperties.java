package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.BasePackage;

/**
 * 代码生成配置基类
 * 
 * @author 	pengpeng
 * @date 	2020年4月21日 下午2:46:51
 */
public abstract class AbstractCodegenConfigProperties {

	/**
	 * 默认代码src目录：当前项目下的src/main/java
	 */
	public static final String DEFAULT_TARGET_PROJECT = "src/main/java";

	/**
	 * 生成代码所在项目的项目根目录
	 */
	private String runtimeProjectDir;

	/**
	 * 项目的基础包名,比如com.xxx
	 */
	private String basePackage = BasePackage.class.getPackage().getName();
	
	/**
	 * 作者
	 */
	private String author = "AutoGenerator";

	public String getRuntimeProjectDir() {
		return runtimeProjectDir;
	}

	public void setRuntimeProjectDir(String runtimeProjectDir) {
		this.runtimeProjectDir = runtimeProjectDir;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
}
