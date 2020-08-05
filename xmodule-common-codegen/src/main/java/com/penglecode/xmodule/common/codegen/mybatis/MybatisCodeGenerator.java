package com.penglecode.xmodule.common.codegen.mybatis;

import com.penglecode.xmodule.common.codeden.consts.CodegenModule;
import com.penglecode.xmodule.common.codeden.support.CustomTableConfiguration;
import com.penglecode.xmodule.common.codegen.config.MybatisCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.MybatisCodegenConfiguration;
import com.penglecode.xmodule.common.codegen.generator.AbstractCodeGenerator;
import com.penglecode.xmodule.common.util.FileUtils;
import com.penglecode.xmodule.common.util.StringUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Mybatis代码生成基类
 * 
 * @author 	pengpeng
 * @date 	2020年4月22日 上午9:16:12
 */
@SuppressWarnings("unchecked")
public class MybatisCodeGenerator extends AbstractCodeGenerator<MybatisCodegenConfiguration> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MybatisCodeGenerator.class);

	/**
	 * 默认的Mybatis代码自动生成XML配置文件位置
	 */
	private static final String DEFAULT_CODEGEN_XML_CONFIG_FILE_LOCATION = "classpath:mybatis-codegen-config.xml";

	/**
	 * 默认的Mybatis代码自动生成XML配置文件中的默认示例表名,需要删除该<table/>配置
	 */
	private static final String DEFAULT_SAMPLE_TABLE_NAME = "sample_table";

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	/**
	 * Mybatis-Generator主要配置文件,例如：classpath:mybatis-codegen-config.xml
	 */
	private final String codegenXmlConfigFileLocation;
	
	/**
	 * 最新生成的代码文件是否覆盖原来代码? (为防代码覆盖丢失，此处直接一棍子打死：永不覆盖！！！)
	 */
	private final boolean overwrite = false;

	public MybatisCodeGenerator(String businessModule) {
		this(DEFAULT_CODEGEN_XML_CONFIG_FILE_LOCATION, businessModule);
	}

	public MybatisCodeGenerator(String codegenXmlConfigFileLocation, String businessModule) {
		super(businessModule);
		this.codegenXmlConfigFileLocation = StringUtils.defaultIfEmpty(codegenXmlConfigFileLocation, DEFAULT_CODEGEN_XML_CONFIG_FILE_LOCATION);
	}

	/**
	 * 覆盖代码生成配置
	 * @param override
	 * @return
	 */
	public MybatisCodeGenerator overrideCodegenConfig(Consumer<MybatisCodegenConfigProperties> override) {
		override.accept(getCodegenConfigProperties());
		return this;
	}

	@Override
	protected void doExecute() throws Exception {
		//读取文件
		Resource codegenXmlConfigFileResource = getCodegenXmlConfigFileResource();
		MybatisCodegenConfigProperties codegenConfigProperties = getCodegenConfigProperties(); //获取代码生成的基础配置
		Properties extraProperties = codegenConfigProperties.asProperties();
		List<String> warnings = new ArrayList<>();
		ConfigurationParser configurationParser = new ConfigurationParser(extraProperties, warnings);
		DefaultShellCallback shellCallback = new DefaultShellCallback(overwrite);
	
		Configuration configuration = configurationParser.parseConfiguration(codegenXmlConfigFileResource.getInputStream());
		List<Context> contexts = configuration.getContexts();
		for(Context context : contexts) {
			List<TableConfiguration> tables = context.getTableConfigurations();
			//删除样例配置<table tableName="sample_table" .../>
			tables.removeIf(tableConfiguration -> DEFAULT_SAMPLE_TABLE_NAME.equalsIgnoreCase(tableConfiguration.getTableName()));
			//添加在application.yaml中数据模型表配置
			Set<CustomTableConfiguration> tableConfigs = codegenConfigProperties.getTables();
			for(CustomTableConfiguration tableConfig : tableConfigs) {
				tables.add(tableConfig.asTableConfiguration(context));
			}
		}
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, shellCallback, warnings);
		myBatisGenerator.generate(null);
		myBatisGenerator.getGeneratedJavaFiles().forEach(file -> {
			String fileName = file.getFileName();
			String filePath = file.getTargetProject() + "/" + file.getTargetPackage().replace(".", "/") + "/" + fileName;
			filePath = FileUtils.normalizePath(filePath);
			LOGGER.info(">>> 生成Mybatis代码成功：[{}, {}]", fileName, filePath);
		});
		myBatisGenerator.getGeneratedXmlFiles().forEach(file -> {
			String fileName = file.getFileName();
			String filePath = file.getTargetProject() + "/" + file.getTargetPackage().replace(".", "/") + "/" + fileName;
			filePath = FileUtils.normalizePath(filePath);
			LOGGER.info(">>> 生成Mybatis代码成功：[{}, {}]", fileName, filePath);
		});
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
