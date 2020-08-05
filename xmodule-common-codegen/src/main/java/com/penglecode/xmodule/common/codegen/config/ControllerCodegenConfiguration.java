package com.penglecode.xmodule.common.codegen.config;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * Controller代码生成配置
 * 
 * @author 	pengpeng
 * @date 	2020年4月15日 下午6:34:45
 */
@Configuration(proxyBeanMethods=false)
@ConfigurationProperties(prefix=ControllerCodegenConfiguration.CODEGEN_CONTROLLER_CONFIGURATION_PREFIX)
public class ControllerCodegenConfiguration extends AbstractCodegenConfiguration<ControllerCodegenConfigProperties> {

	public static final String CODEGEN_CONTROLLER_CONFIGURATION_PREFIX = "spring.codegen.controller";
	
	@Override
	public ControllerCodegenConfigProperties getCodegenConfigProperties(String businessModule) {
		ControllerCodegenConfigProperties configProperties = super.getCodegenConfigProperties(businessModule);
		Assert.notNull(configProperties, String.format("Modular controller codegen config '%s.config.%s.*' not found!", CODEGEN_CONTROLLER_CONFIGURATION_PREFIX, businessModule));
		return configProperties;
	}

	@Override
	public void init() throws Exception {
		for(Map.Entry<String,ControllerCodegenConfigProperties> entry : getConfig().entrySet()) {
			String moduleName = entry.getKey();
			ControllerCodegenConfigProperties moduleProperties = entry.getValue();
			Assert.hasText(moduleProperties.getTargetPackage(), String.format("Controller codegen config '%s.config.%s.targetPackage' must be required!", CODEGEN_CONTROLLER_CONFIGURATION_PREFIX, moduleName));
			Assert.hasText(moduleProperties.getTargetProject(), String.format("Controller codegen config '%s.config.%s.targetProject' must be required!", CODEGEN_CONTROLLER_CONFIGURATION_PREFIX, moduleName));
			
			File targetProjectDir = Paths.get(getRuntimeProjectDir(), moduleProperties.getTargetProject()).toFile();
			Assert.isTrue(targetProjectDir.exists(), String.format("Controller codegen config '%s.config.%s.targetPackage' indicated directory dos not exists!", CODEGEN_CONTROLLER_CONFIGURATION_PREFIX, moduleName));
			moduleProperties.setTargetProject(targetProjectDir.getAbsolutePath());
		}
	}
	
}
