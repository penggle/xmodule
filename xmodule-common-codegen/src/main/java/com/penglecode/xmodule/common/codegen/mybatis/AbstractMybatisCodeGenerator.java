package com.penglecode.xmodule.common.codegen.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.codeden.consts.CodegenModule;
import com.penglecode.xmodule.common.codegen.config.MybatisCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.MybatisCodegenConfiguration;
import com.penglecode.xmodule.common.codegen.generator.AbstractCodeGenerator;

/**
 * Mybatis代码生成基类
 * 
 * @author 	pengpeng
 * @date 	2020年4月22日 上午9:16:12
 */
@SuppressWarnings("unchecked")
public abstract class AbstractMybatisCodeGenerator extends AbstractCodeGenerator<MybatisCodegenConfiguration> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMybatisCodeGenerator.class);
	
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	/**
	 * Mybatis-Generator主要配置文件,例如：classpath:mybatis-codegen-config-order.xml
	 */
	private final String codegenXmlConfigFileLocation;
	
	/**
	 * 最新生成的代码文件是否覆盖原来代码? (为防代码覆盖丢失，此处直接一棍子打死：永不覆盖！！！)
	 */
	private final boolean overwrite = false;
	
	public AbstractMybatisCodeGenerator(String codegenXmlConfigFileLocation, String businessModule) {
		super(businessModule);
		Assert.hasText(codegenXmlConfigFileLocation, "Parameter 'codegenXmlConfigFileLocation' must be required!");
		this.codegenXmlConfigFileLocation = codegenXmlConfigFileLocation;
	}

	@Override
	protected void doGenerate() throws Exception {
		//读取文件
		Resource codegenXmlConfigFileResource = getCodegenXmlConfigFileResource();
		MybatisCodegenConfigProperties codegenConfigProperties = getCodegenConfigProperties(); //获取代码生成的基础配置
		Properties extraProperties = codegenConfigProperties.asProperties();
		List<String> warnings = new ArrayList<String>();
		ConfigurationParser configurationParser = new ConfigurationParser(extraProperties, warnings);
		DefaultShellCallback shellCallback = new DefaultShellCallback(overwrite);
	
		Configuration configuration = configurationParser.parseConfiguration(codegenXmlConfigFileResource.getInputStream());
		List<Context> contexts = configuration.getContexts();
		
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, shellCallback, warnings);
		myBatisGenerator.generate(null);
		for(Context context : contexts) {
			List<TableConfiguration> tables = context.getTableConfigurations();
			for(TableConfiguration table : tables) {
				String modelName = table.getDomainObjectName();
				LOGGER.info(">>> 生成Mybatis代码[{}.java, {}Mapper.java, {}Mapper.xml]成功!", modelName, modelName, modelName);
			}
		}
	}

	@Override
	protected CodegenModule getCodeModule() {
		return CodegenModule.MYBATIS;
	}
	
	@Override
	protected MybatisCodegenConfigProperties getCodegenConfigProperties() {
		return super.getCodegenConfigProperties();
	}

	protected Resource getCodegenXmlConfigFileResource() {
		return getResourcePatternResolver().getResource(codegenXmlConfigFileLocation);
	}

	protected ResourcePatternResolver getResourcePatternResolver() {
		return resourcePatternResolver;
	}

	public void setResourcePatternResolver(ResourcePatternResolver resourcePatternResolver) {
		this.resourcePatternResolver = resourcePatternResolver;
	}
	
	protected String getCodegenXmlConfigFileLocation() {
		return codegenXmlConfigFileLocation;
	}

	protected boolean isOverwrite() {
		return overwrite;
	}

}
