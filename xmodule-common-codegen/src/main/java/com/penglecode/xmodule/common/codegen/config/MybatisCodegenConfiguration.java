package com.penglecode.xmodule.common.codegen.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.JdbcUtils;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * Mybatis代码生成配置
 * 
 * @author 	pengpeng
 * @date 	2020年4月15日 下午6:34:45
 */
@Configuration(proxyBeanMethods=false)
@ConfigurationProperties(prefix=MybatisCodegenConfiguration.CODEGEN_MYBATIS_CONFIGURATION_PREFIX)
public class MybatisCodegenConfiguration extends AbstractCodegenConfiguration<MybatisCodegenConfigProperties> {

	public static final String CODEGEN_MYBATIS_CONFIGURATION_PREFIX = "spring.codegen.mybatis";
	
	@Override
	public MybatisCodegenConfigProperties getCodegenConfigProperties(String businessModule) {
		MybatisCodegenConfigProperties configProperties = super.getCodegenConfigProperties(businessModule);
		Assert.notNull(configProperties, String.format("Modular mybatis codegen config '%s.config.%s.*' not found!", CODEGEN_MYBATIS_CONFIGURATION_PREFIX, businessModule));
		return configProperties;
	}
	
	@Override
	public void init() throws Exception {
		if(!CollectionUtils.isEmpty(getConfig())) {
			for(Map.Entry<String,MybatisCodegenConfigProperties> entry : getConfig().entrySet()) {
				String moduleName = entry.getKey();
				MybatisCodegenConfigProperties moduleConfig = entry.getValue();
				String dataSourceName = moduleConfig.getDataSourceName();
				if(!StringUtils.isEmpty(dataSourceName)) {
					String jdbcUrl = getEnvironment().getProperty("spring.datasource." + dataSourceName + ".url");
					String jdbcUsername = getEnvironment().getProperty("spring.datasource." + dataSourceName + ".username");
					String jdbcPassword = getEnvironment().getProperty("spring.datasource." + dataSourceName + ".password");
					if(!StringUtils.isEmpty(jdbcUrl) && !StringUtils.isEmpty(jdbcUsername) && !StringUtils.isEmpty(jdbcPassword)) {
						moduleConfig.setJdbcUrl(jdbcUrl);
						moduleConfig.setJdbcDriverClass(JdbcUtils.getDriverClassName(jdbcUrl));
						moduleConfig.setJdbcUsername(jdbcUsername);
						moduleConfig.setJdbcPassword(jdbcPassword);
					}
				}
				Assert.hasText(moduleConfig.getJdbcUrl(), String.format("Mybatis codegen config '%s.config.%s.jdbcUrl' must be required!", CODEGEN_MYBATIS_CONFIGURATION_PREFIX, moduleName));
				Assert.hasText(moduleConfig.getJdbcDriverClass(), String.format("Mybatis codegen config '%s.config.%s.jdbcDriverClass' must be required!", CODEGEN_MYBATIS_CONFIGURATION_PREFIX, moduleName));
				Assert.hasText(moduleConfig.getJdbcUsername(), String.format("Mybatis codegen config '%s.config.%s.jdbcUsername' must be required!", CODEGEN_MYBATIS_CONFIGURATION_PREFIX, moduleName));
				Assert.hasText(moduleConfig.getJdbcPassword(), String.format("Mybatis codegen config '%s.config.%s.jdbcPassword' must be required!", CODEGEN_MYBATIS_CONFIGURATION_PREFIX, moduleName));
				
				Assert.hasText(moduleConfig.getJavaModel().getTargetPackage(), String.format("Mybatis codegen config '%s.config.%s.javaModel.targetPackage' must be required!", CODEGEN_MYBATIS_CONFIGURATION_PREFIX, moduleName));
				Assert.hasText(moduleConfig.getXmlMapper().getTargetPackage(), String.format("Mybatis codegen config '%s.config.%s.xmlMapper.targetPackage' must be required!", CODEGEN_MYBATIS_CONFIGURATION_PREFIX, moduleName));
				Assert.hasText(moduleConfig.getDaoMapper().getTargetPackage(), String.format("Mybatis codegen config '%s.config.%s.daoMapper.targetPackage' must be required!", CODEGEN_MYBATIS_CONFIGURATION_PREFIX, moduleName));
			}
		}
	}
	
}
