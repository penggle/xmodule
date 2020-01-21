package com.penglecode.xmodule.common.codegen.mybatis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 执行代码生成的基类,每个项目模块需要继承改基类,例如：
 * 
 * public class CustomMybatisCodeGenerator extends AbstractMybatisCodeGenerator {
 * 
 * 		public static void main(String[] args) {
 * 			CustomMybatisCodeGenerator generator = new CustomMybatisCodeGenerator();
 * 			generator.run();
 * 		}
 * 
 * }
 * 
 * @author 	pengpeng
 * @date	2018年2月10日 下午4:48:57
 */
public abstract class AbstractMybatisCodeGenerator {

	private static final String DEFAULT_GENERATOR_CONFIG_FILE_LOCATION = "classpath:generatorConfig.xml";
	
	private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
	
	private final String generatorConfigFileLocation;
	
	private boolean overwrite = false;
	
	public AbstractMybatisCodeGenerator() {
		this(DEFAULT_GENERATOR_CONFIG_FILE_LOCATION);
	}
	
	public AbstractMybatisCodeGenerator(String generatorConfigFileLocation) {
		super();
		this.generatorConfigFileLocation = generatorConfigFileLocation;
	}

	public void run() {
		try {
			//读取文件
			Resource configFileResource = RESOURCE_PATTERN_RESOLVER.getResource(generatorConfigFileLocation);
			File configFile = configFileResource.getFile();
			List<String> warnings = new ArrayList<String>();
			ConfigurationParser cp = new ConfigurationParser(warnings);
			Configuration config = null;
			DefaultShellCallback callback = new DefaultShellCallback(overwrite);
			MyBatisGenerator myBatisGenerator = null;
		
			config = cp.parseConfiguration(configFile);
			myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
			myBatisGenerator.generate(null);
			System.out.println(">>> 代码成功生成!");
		} catch (Exception e) {
			System.err.println(">>> 代码生成出错：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String getGeneratorConfigFileLocation() {
		return generatorConfigFileLocation;
	}

	public boolean isOverwrite() {
		return overwrite;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
	
}
