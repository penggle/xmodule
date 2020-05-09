package com.penglecode.xmodule.common.codegen.config;

/**
 * 共通的代码生成配置
 * 
 * @author 	pengpeng
 * @date 	2020年4月16日 下午5:11:17
 */
public class CommonCodegenConfigProperties extends AbstractCodegenConfigProperties {

	/**
	 * 代码输出的项目位置
	 */
	private String targetProject = DEFAULT_TARGET_PROJECT;
	
	/**
	 * 代码输出的包路径
	 */
	private String targetPackage;
	
	public String getTargetProject() {
		return targetProject;
	}

	public void setTargetProject(String targetProject) {
		this.targetProject = targetProject;
	}

	public String getTargetPackage() {
		return targetPackage;
	}

	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}

	@Override
	public String toString() {
		return "{targetProject=" + targetProject + ", targetPackage=" + targetPackage
				+ ", basePackage=" + getBasePackage() + ", author=" + getAuthor() + "}";
	}

}
