package com.penglecode.xmodule.common.codegen.service;

import com.penglecode.xmodule.common.util.StringUtils;

/**
 * 服务代码生成配置
 * 
 * @author 	pengpeng
 * @date	2019年3月1日 下午4:49:09
 */
public class ServiceCodeConfig {

	/**
	 * 默认项目源码所在位置
	 */
	public static final String DEFAULT_PROJECT_SOURCE_LOCATION = "src/main/java";
	
	/**
	 * 项目的基础包名,比如com.alibaba.dubbo
	 */
	private final String projectBasePackage;
	
	/**
	 * 服务接口所在包
	 */
	private final String serviceInterfacePackage;
	
	/**
	 * 服务实现所在包
	 */
	private final String serviceImplementationPackage;
	
	/**
	 * 项目源码位置
	 */
	private final String projectSourceLocation;
	
	/**
	 * 作者
	 */
	private String author = "AutoGen";
	
	/**
	 * 服务接口所在目录
	 */
	private String serviceInterfaceDir;
	
	/**
	 * 服务实现所在目录
	 */
	private String serviceImplementationDir;
	
	public ServiceCodeConfig(String projectBasePackage, String serviceInterfacePackage, String serviceImplementationPackage) {
		this(projectBasePackage, serviceInterfacePackage, serviceImplementationPackage, DEFAULT_PROJECT_SOURCE_LOCATION);
	}

	public ServiceCodeConfig(String projectBasePackage, String serviceInterfacePackage, String serviceImplementationPackage,
			String projectSourceLocation) {
		super();
		this.projectBasePackage = projectBasePackage;
		this.serviceInterfacePackage = serviceInterfacePackage;
		this.serviceImplementationPackage = serviceImplementationPackage;
		this.projectSourceLocation = StringUtils.defaultIfEmpty(projectSourceLocation, DEFAULT_PROJECT_SOURCE_LOCATION);
	}

	public String getProjectBasePackage() {
		return projectBasePackage;
	}

	public String getServiceInterfacePackage() {
		return serviceInterfacePackage;
	}

	public String getServiceImplementationPackage() {
		return serviceImplementationPackage;
	}

	public String getProjectSourceLocation() {
		return projectSourceLocation;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getServiceInterfaceDir() {
		return serviceInterfaceDir;
	}

	public void setServiceInterfaceDir(String serviceInterfaceDir) {
		this.serviceInterfaceDir = serviceInterfaceDir;
	}

	public String getServiceImplementationDir() {
		return serviceImplementationDir;
	}

	public void setServiceImplementationDir(String serviceImplementationDir) {
		this.serviceImplementationDir = serviceImplementationDir;
	}

}
