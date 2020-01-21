package com.penglecode.xmodule.common.cloud.consul;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.consul.config.ConsulConfigProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.penglecode.xmodule.common.exception.ApplicationRuntimeException;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.util.YamlPropsUtils;

/**
 * 基于Consul的配置中心服务
 * 
 * 该服务仅适用于以下配置的Consul配置中心：
 * 
 * 		#以下三项联合配置对应的consul配置文件key为:config/${spring.application.name}/application-(dev|prd).(yml|yaml|properties)
 * 		prefix: config/${spring.application.name}
 * 		defaultContext: application
 * 		profileSeparator: '-'
 * 		format: FILES
 * 
 * @author 	pengpeng
 * @date	2019年8月7日 上午9:49:39
 */
public class ConsulConfigService implements EnvironmentAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsulConfigService.class);
	
	private static final List<String> PROFILE_FORMATS = Arrays.asList(".yml", ".yaml", ".properties");
	
	private Environment environment;
	
	@Autowired
	private ConsulClient consulClient;
	
	@Autowired
	private ConsulConfigProperties consulConfigProperties;
	
	/**
	 * 发布配置
	 * @param springAppName		- springboot应用的名字,一般是${spring.application.name}
	 * @param profile			- 配置文件名称，例如：application-(dev|prd).(yml|yaml|properties)
	 * @param config			- 基于map形式的增量配置，例如：{"app.id": 1, "app.name": "myapp"}
	 */
	public void publishConfig(String springAppName, String profile, Map<String,Object> config) {
		Assert.hasText(springAppName, "Parameter 'springAppName' can not be empty!");
		Assert.hasText(profile, "Parameter 'profile' can not be empty!");
		Assert.notEmpty(config, "Parameter 'config' can not be empty!");
		profile = profile.toLowerCase();
		String suffix = profile.substring(profile.lastIndexOf('.'));
		Assert.isTrue(PROFILE_FORMATS.contains(suffix), String.format("Parameter 'profile' must be endswith: %s", PROFILE_FORMATS));
		String profileKey = getProfileKey(springAppName, profile);
		LOGGER.info(">>> prepare to publish application profile: {}", profileKey);
		String aclToken = consulConfigProperties.getAclToken();
		//1、先加载全部配置
		Response<GetValue> getResponse = consulClient.getKVValue(profileKey, aclToken);
		Map<String,Object> allConfig = getMapProfile(suffix, getResponse.getValue());
		//2、覆盖更新配置
		
		String propsConfigContent = YamlPropsUtils.map2Props(allConfig);
		Properties propsConfig = new Properties();
		try {
			propsConfig.load(new StringReader(propsConfigContent));
		} catch (IOException e) {
			throw new ApplicationRuntimeException(e.getMessage(), e);
		}
		
		//合并/覆盖配置
		for(Map.Entry<String,Object> entry : config.entrySet()) {
			propsConfig.put(entry.getKey(), entry.getValue());
		}
		
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<Object,Object> entry : propsConfig.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue() + "\n");
		}
		
		String newPropsConfigContent = sb.toString();
		newPropsConfigContent = StringUtils.stripEnd(newPropsConfigContent, "\n");
		
		//properties配置转map
		allConfig = YamlPropsUtils.props2Map(newPropsConfigContent);
		
		//3、发布配置更新到远程consul
		String profileText = getTextProfile(suffix, allConfig);
		Response<Boolean> setResponse = consulClient.setKVValue(profileKey, profileText, aclToken, null);
		LOGGER.info("<<< {} to publish application profile: {}", setResponse.getValue() ? "succeed" : "failed", profileKey);
	}
	
	/**
	 * 发布配置
	 * @param springAppName		- springboot应用的名字,一般是${spring.application.name}
	 * @param profile			- 配置文件名称，例如：application-(dev|prd).(yml|yaml|properties)
	 * @param content			- 基于文件形式的全量配置
	 */
	public void publishConfig(String springAppName, String profile, String content) {
		Assert.hasText(springAppName, "Parameter 'springAppName' can not be empty!");
		Assert.hasText(profile, "Parameter 'profile' can not be empty!");
		Assert.hasText(content, "Parameter 'content' can not be empty!");
		profile = profile.toLowerCase();
		String suffix = profile.substring(profile.lastIndexOf('.'));
		Assert.isTrue(PROFILE_FORMATS.contains(suffix), String.format("Parameter 'profile' must be endswith: %s", PROFILE_FORMATS));
		String profileKey = getProfileKey(springAppName, profile);
		LOGGER.info(">>> prepare to publish application profile: {}", profileKey);
		String aclToken = consulConfigProperties.getAclToken();
		//3、发布配置更新到远程consul
		String profileText = content.replaceAll("\t", "    "); //替换制表符为四个空格防止yaml报错
		Response<Boolean> setResponse = consulClient.setKVValue(profileKey, profileText, aclToken, null);
		LOGGER.info("<<< {} to publish application profile: {}", setResponse.getValue() ? "succeed" : "failed", profileKey);
	}
	
	/**
	 * 删除配置
	 * @param springAppName		- springboot应用的名字,一般是${spring.application.name}
	 * @param profile			- 配置文件名称，例如：application-(dev|prd).(yml|yaml|properties)
	 */
	public void deleteConfig(String springAppName, String profile) {
		Assert.hasText(springAppName, "Parameter 'springAppName' can not be empty!");
		Assert.hasText(profile, "Parameter 'profile' can not be empty!");
		profile = profile.toLowerCase();
		String suffix = profile.substring(profile.lastIndexOf('.'));
		Assert.isTrue(PROFILE_FORMATS.contains(suffix), String.format("Parameter 'profile' must be endswith: %s", PROFILE_FORMATS));
		String profileKey = getProfileKey(springAppName, profile);
		LOGGER.info(">>> prepare to delete application profile: {}", profileKey);
		String aclToken = consulConfigProperties.getAclToken();
		consulClient.deleteKVValue(profileKey, aclToken);
		LOGGER.info("<<< {} to delete application profile: {}", "succeed", profileKey);
	}
	
	/**
	 * 获取配置
	 * @param springAppName		- springboot应用的名字,一般是${spring.application.name}
	 * @param profile			- 配置文件名称，例如：application-(dev|prd).(yml|yaml|properties)
	 */
	public Map<String,Object> getMapConfig(String springAppName, String profile) {
		Assert.hasText(springAppName, "Parameter 'springAppName' can not be empty!");
		Assert.hasText(profile, "Parameter 'profile' can not be empty!");
		profile = profile.toLowerCase();
		String suffix = profile.substring(profile.lastIndexOf('.'));
		Assert.isTrue(PROFILE_FORMATS.contains(suffix), String.format("Parameter 'profile' must be endswith: %s", PROFILE_FORMATS));
		String profileKey = getProfileKey(springAppName, profile);
		LOGGER.info(">>> prepare to publish application profile: {}", profileKey);
		String aclToken = consulConfigProperties.getAclToken();
		Response<GetValue> getResponse = consulClient.getKVValue(profileKey, aclToken);
		return getMapProfile(suffix, getResponse.getValue());
	}
	
	/**
	 * 获取配置
	 * @param springAppName		- springboot应用的名字,一般是${spring.application.name}
	 * @param profile			- 配置文件名称，例如：application-(dev|prd).(yml|yaml|properties)
	 */
	public String getTextConfig(String springAppName, String profile) {
		Assert.hasText(springAppName, "Parameter 'springAppName' can not be empty!");
		Assert.hasText(profile, "Parameter 'profile' can not be empty!");
		profile = profile.toLowerCase();
		String suffix = profile.substring(profile.lastIndexOf('.'));
		Assert.isTrue(PROFILE_FORMATS.contains(suffix), String.format("Parameter 'profile' must be endswith: %s", PROFILE_FORMATS));
		String profileKey = getProfileKey(springAppName, profile);
		LOGGER.info(">>> prepare to publish application profile: {}", profileKey);
		String aclToken = consulConfigProperties.getAclToken();
		Response<GetValue> getResponse = consulClient.getKVValue(profileKey, aclToken);
		GetValue getValue = getResponse.getValue();
		return getValue == null ? null : getValue.getDecodedValue();
	}
	
	protected String getProfileKey(String springAppName, String profile) {
		String profileKey = consulConfigProperties.getPrefix() + "/" + profile;
		String thisAppName = environment.getProperty("spring.application.name");
		if(!StringUtils.isEmpty(thisAppName)) {
			profileKey = profileKey.replace(thisAppName, springAppName);
		}
		return profileKey;
	}
	
	protected Map<String,Object> getMapProfile(String suffix, GetValue getValue) {
		Map<String,Object> allConfig = new HashMap<String,Object>();
		if(getValue != null) {
			String value = getValue.getDecodedValue();
			if(!StringUtils.isEmpty(value)) {
				Map<String,Object> config = null;
				if(".properties".equals(suffix)) { //properties file
					config = YamlPropsUtils.props2Map(value);
				} else { //yaml file
					config = YamlPropsUtils.yaml2Map(value);
				}
				if(!CollectionUtils.isEmpty(config)) {
					allConfig.putAll(config);
				}
			}
		}
		return allConfig;
	}
	
	protected String getTextProfile(String suffix, Map<String,Object> allConfig) {
		if(".properties".equals(suffix)) { //properties file
			return YamlPropsUtils.map2Props(allConfig);
		} else { //yaml file
			return YamlPropsUtils.map2Yaml(allConfig);
		}
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
}
