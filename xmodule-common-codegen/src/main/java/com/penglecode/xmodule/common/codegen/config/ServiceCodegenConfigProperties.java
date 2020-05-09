package com.penglecode.xmodule.common.codegen.config;

/**
 * Service代码生成配置
 * 
 * @author 	pengpeng
 * @date 	2020年4月16日 下午5:23:27
 */
public class ServiceCodegenConfigProperties extends AbstractCodegenConfigProperties {

	/**
	 * 服务接口配置
	 */
	private CommonCodegenConfigProperties jinterface = new CommonCodegenConfigProperties();
	
	/**
	 * 服务实现配置
	 */
	private CommonCodegenConfigProperties jimplement = new CommonCodegenConfigProperties();

	public ServiceCodegenConfigProperties() {
		super();
	}

	public CommonCodegenConfigProperties getJinterface() {
		return jinterface;
	}

	public void setJinterface(CommonCodegenConfigProperties jinterface) {
		this.jinterface = jinterface;
	}

	public CommonCodegenConfigProperties getJimplement() {
		return jimplement;
	}

	public void setJimplement(CommonCodegenConfigProperties jimplement) {
		this.jimplement = jimplement;
	}
	
}
