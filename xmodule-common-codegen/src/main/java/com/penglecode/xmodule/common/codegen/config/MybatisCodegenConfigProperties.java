package com.penglecode.xmodule.common.codegen.config;

import com.penglecode.xmodule.common.codeden.support.CustomTableConfiguration;
import com.penglecode.xmodule.common.util.StringUtils;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Mybatis代码生成配置
 * 
 * @author 	pengpeng
 * @date 	2020年4月16日 下午4:41:05
 */
public class MybatisCodegenConfigProperties extends AbstractCodegenConfigProperties {
	
	/**
	 * 使用的数据源名称
	 */
	private String dataSourceName;
	
	/**
	 * JDBC驱动名称
	 */
	private String jdbcDriverClass;
	
	/**
	 * JDBC URL
	 */
	private String jdbcUrl;
	
	/**
	 * JDBC username
	 */
	private String jdbcUsername;
	
	/**
	 * JDBC password
	 */
	private String jdbcPassword;
	
	/**
	 * model源码配置
	 */
	private CommonCodegenConfigProperties javaModel = new CommonCodegenConfigProperties();
	
	/**
	 * XxxMapper.xml配置
	 */
	private CommonCodegenConfigProperties xmlMapper = new CommonCodegenConfigProperties();
	
	/**
	 * XxxMapper.java源码配置
	 */
	private CommonCodegenConfigProperties daoMapper = new CommonCodegenConfigProperties();

	/**
	 * 表配置定义
	 */
	private Set<CustomTableConfiguration> tables = new HashSet<>();
	
	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public String getJdbcDriverClass() {
		return jdbcDriverClass;
	}

	public void setJdbcDriverClass(String jdbcDriverClass) {
		this.jdbcDriverClass = jdbcDriverClass;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getJdbcUsername() {
		return jdbcUsername;
	}

	public void setJdbcUsername(String jdbcUsername) {
		this.jdbcUsername = jdbcUsername;
	}

	public String getJdbcPassword() {
		return jdbcPassword;
	}

	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	public CommonCodegenConfigProperties getJavaModel() {
		return javaModel;
	}

	public void setJavaModel(CommonCodegenConfigProperties javaModel) {
		this.javaModel = javaModel;
	}

	public CommonCodegenConfigProperties getXmlMapper() {
		return xmlMapper;
	}

	public void setXmlMapper(CommonCodegenConfigProperties xmlMapper) {
		this.xmlMapper = xmlMapper;
	}

	public CommonCodegenConfigProperties getDaoMapper() {
		return daoMapper;
	}

	public void setDaoMapper(CommonCodegenConfigProperties daoMapper) {
		this.daoMapper = daoMapper;
	}

	public Set<CustomTableConfiguration> getTables() {
		return tables;
	}

	public void setTables(Set<CustomTableConfiguration> tables) {
		this.tables = tables;
	}

	public Properties asProperties() {
		Properties properties = new Properties();
		if(!StringUtils.isEmpty(jdbcUrl)) {
			properties.put("jdbcUrl", jdbcUrl);
		}
		if(!StringUtils.isEmpty(jdbcDriverClass)) {
			properties.put("jdbcDriverClass", jdbcDriverClass);
		}
		if(!StringUtils.isEmpty(jdbcUsername)) {
			properties.put("jdbcUsername", jdbcUsername);
		}
		if(!StringUtils.isEmpty(jdbcPassword)) {
			properties.put("jdbcPassword", jdbcPassword);
		}
		if(!StringUtils.isEmpty(javaModel.getTargetProject())) {
			properties.put("javaModelTargetProject", javaModel.getTargetProject());
		}
		if(!StringUtils.isEmpty(javaModel.getTargetPackage())) {
			properties.put("javaModelTargetPackage", javaModel.getTargetPackage());
		}
		if(!StringUtils.isEmpty(xmlMapper.getTargetProject())) {
			properties.put("xmlMapperTargetProject", xmlMapper.getTargetProject());
		}
		if(!StringUtils.isEmpty(xmlMapper.getTargetPackage())) {
			properties.put("xmlMapperTargetPackage", xmlMapper.getTargetPackage());
		}
		if(!StringUtils.isEmpty(daoMapper.getTargetProject())) {
			properties.put("daoMapperTargetProject", daoMapper.getTargetProject());
		}
		if(!StringUtils.isEmpty(daoMapper.getTargetPackage())) {
			properties.put("daoMapperTargetPackage", daoMapper.getTargetPackage());
		}
		return properties;
	}

	@Override
	public String toString() {
		return "{dataSourceName=" + dataSourceName + ", jdbcDriverClass="
				+ jdbcDriverClass + ", jdbcUrl=" + jdbcUrl + ", jdbcUsername=" + jdbcUsername + ", jdbcPassword="
				+ jdbcPassword + ", javaModel=" + javaModel + ", xmlMapper=" + xmlMapper + ", daoMapper=" + daoMapper
				+ "}";
	}
	
}