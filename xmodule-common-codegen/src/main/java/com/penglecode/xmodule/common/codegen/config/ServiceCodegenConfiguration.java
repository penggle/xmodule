package com.penglecode.xmodule.common.codegen.config;

import java.io.File;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * Service代码生成配置
 * 
 * @author 	pengpeng
 * @date 	2020年4月15日 下午6:34:45
 */
@Configuration(proxyBeanMethods=false)
@ConfigurationProperties(prefix=ServiceCodegenConfiguration.CODEGEN_SERVICE_CONFIGURATION_PREFIX)
public class ServiceCodegenConfiguration extends AbstractCodegenConfiguration<ServiceCodegenConfigProperties> {

	public static final String CODEGEN_SERVICE_CONFIGURATION_PREFIX = "spring.codegen.service";
	
	@Override
	public ServiceCodegenConfigProperties getCodegenConfigProperties(String businessModule) {
		ServiceCodegenConfigProperties configProperties = super.getCodegenConfigProperties(businessModule);
		Assert.notNull(configProperties, String.format("Modular service codegen config '%s.config.%s.*' not found!", CODEGEN_SERVICE_CONFIGURATION_PREFIX, businessModule));
		return configProperties;
	}

	@Override
	public void init() throws Exception {
		for(Map.Entry<String,ServiceCodegenConfigProperties> entry : getConfig().entrySet()) {
			String moduleName = entry.getKey();
			ServiceCodegenConfigProperties moduleProperties = entry.getValue();
			Assert.notNull(moduleProperties.getJinterface(), String.format("Service codegen config '%s.config.%s.jinterface' must be required!", CODEGEN_SERVICE_CONFIGURATION_PREFIX, moduleName));
			Assert.hasText(moduleProperties.getJinterface().getTargetPackage(), String.format("Service codegen config '%s.config.%s.jinterface.targetPackage' must be required!", CODEGEN_SERVICE_CONFIGURATION_PREFIX, moduleName));
			Assert.hasText(moduleProperties.getJinterface().getTargetProject(), String.format("Service codegen config '%s.config.%s.jinterface.targetProject' must be required!", CODEGEN_SERVICE_CONFIGURATION_PREFIX, moduleName));
			Assert.notNull(moduleProperties.getJimplement(), String.format("Service codegen config '%s.config.%s.jimplement' must be required!", CODEGEN_SERVICE_CONFIGURATION_PREFIX, moduleName));
			Assert.hasText(moduleProperties.getJimplement().getTargetPackage(), String.format("Service codegen config '%s.config.%s.jimplement.targetPackage' must be required!", CODEGEN_SERVICE_CONFIGURATION_PREFIX, moduleName));
			Assert.hasText(moduleProperties.getJimplement().getTargetProject(), String.format("Service codegen config '%s.config.%s.jimplement.targetProject' must be required!", CODEGEN_SERVICE_CONFIGURATION_PREFIX, moduleName));
			
			File interfaceTargetProjectDir = new File(moduleProperties.getJinterface().getTargetProject());
			Assert.isTrue(interfaceTargetProjectDir.exists(), String.format("Service codegen config '%s.config.%s.jinterface.targetPackage' indicated directory dos not exists!", CODEGEN_SERVICE_CONFIGURATION_PREFIX, moduleName));
			moduleProperties.getJinterface().setTargetProject(interfaceTargetProjectDir.getAbsolutePath());
			
			File implementTargetProjectDir = new File(moduleProperties.getJimplement().getTargetProject());
			Assert.isTrue(implementTargetProjectDir.exists(), String.format("Service codegen config '%s.config.%s.jimplement.targetPackage' indicated directory dos not exists!", CODEGEN_SERVICE_CONFIGURATION_PREFIX, moduleName));
			moduleProperties.getJimplement().setTargetProject(implementTargetProjectDir.getAbsolutePath());
		}
	}
	
}
