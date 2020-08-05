package com.penglecode.xmodule.common.codegen.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * 代码生成配置基类
 * @param <T>
 * @author 	pengpeng
 * @date 	2020年4月21日 下午3:16:53
 */
public abstract class AbstractCodegenConfiguration<T extends AbstractCodegenConfigProperties> implements EnvironmentAware, InitializingBean {

	/**
	 * 生成代码所在项目的项目根目录
	 */
	private final String runtimeProjectDir;

	/**
	 * Spring环境变量
	 */
	private Environment environment;
	
	/**
	 * 分模块的代码生成配置
	 */
	private Map<String, T> config = new HashMap<>();

	public AbstractCodegenConfiguration() {
		String rootDir = getClass().getResource("/").getPath();
		rootDir = rootDir.substring(1, rootDir.indexOf("/target/"));
		this.runtimeProjectDir = rootDir;
	}

	public String getRuntimeProjectDir() {
		return runtimeProjectDir;
	}

	public Map<String, T> getConfig() {
		return config;
	}

	public void setConfig(Map<String, T> config) {
		this.config = config;
	}

	/**
	 * 初始化，例如对配置进行必要的验证等
	 * @throws Exception
	 */
	protected abstract void init() throws Exception;
	
	/**
	 * 获取指定业务模块的代码生成基本配置
	 * @param businessModule
	 * @return
	 */
	protected T getCodegenConfigProperties(String businessModule) {
		return getConfig().get(businessModule);
	}
	
	public Environment getEnvironment() {
		return environment;
	}

	@Override
	public final void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	@Override
	public final void afterPropertiesSet() throws Exception {
		config.forEach((module, cfg) -> {
			cfg.setRuntimeProjectDir(getRuntimeProjectDir());
		});
		init();
	}
	
}
