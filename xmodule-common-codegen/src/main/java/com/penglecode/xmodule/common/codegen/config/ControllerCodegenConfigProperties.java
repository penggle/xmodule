package com.penglecode.xmodule.common.codegen.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller代码生成配置
 * 
 * @author 	pengpeng
 * @date 	2020年4月16日 下午5:23:27
 */
public class ControllerCodegenConfigProperties extends CommonCodegenConfigProperties {

	/**
	 * Controller中各个handler方法的公共URL前缀
	 */
	private String prefixOfApiUrl = "/api";
	
	/**
	 * Controller中各个handler方法的URL的domain，默认取@Model(alias="xxx")注解中alias的小写形式，用于生成接口的URL
	 * 例如：/api/${domain}/list
	 */
	private Map<String,String> domainOfApiUrlMapping = new HashMap<>();
	
	/**
	 * Controller的公共继承类
	 */
	private String extendsClass;

	/**
	 * 是否生成API文档
	 */
	private boolean generateApiDocs;

	public String getExtendsClass() {
		return extendsClass;
	}

	public void setExtendsClass(String extendsClass) {
		this.extendsClass = extendsClass;
	}

	public String getPrefixOfApiUrl() {
		return prefixOfApiUrl;
	}

	public void setPrefixOfApiUrl(String prefixOfApiUrl) {
		this.prefixOfApiUrl = prefixOfApiUrl;
	}

	public Map<String, String> getDomainOfApiUrlMapping() {
		return domainOfApiUrlMapping;
	}

	public void setDomainOfApiUrlMapping(Map<String, String> domainOfApiUrlMapping) {
		this.domainOfApiUrlMapping = domainOfApiUrlMapping;
	}

}