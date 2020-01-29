package com.penglecode.xmodule.security.oauth2.examples.web.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.security.oauth2.examples.config.HydraOAuth2LoginConfigProperties;

import sh.ory.hydra.api.AdminApi;
import sh.ory.hydra.model.OAuth2Client;

@RestController
@RequestMapping("/api/hydra")
public class OAuth2HydraServerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2HydraServerController.class);
	
	@Autowired
	private AdminApi hydraAdminApi;
	
	@Autowired
	private HydraOAuth2LoginConfigProperties hydraOAuth2LoginConfig;
	
	@GetMapping(value="/init", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> init() throws Exception {
		LOGGER.info(">>> 重新初始化Hydra服务端配置开始...");
		removeClient();
		createClient();
		return Result.success().build();
	}
	
	/**
	 * 删除Client
	 */
	protected void removeClient() throws Exception {
		hydraAdminApi.deleteOAuth2Client(hydraOAuth2LoginConfig.getClientId());
		LOGGER.info(">>> 删除Client({})成功!", hydraOAuth2LoginConfig.getClientId()); 
	}
	
	/**
	 * 创建Client
	 */
	protected void createClient() throws Exception {
		OAuth2Client oauth2Client = new OAuth2Client();
		oauth2Client.setClientId(hydraOAuth2LoginConfig.getClientId());
		oauth2Client.setClientSecret(hydraOAuth2LoginConfig.getClientSecret());
		oauth2Client.setClientName(hydraOAuth2LoginConfig.getClientId());
		oauth2Client.setGrantTypes(Arrays.asList(hydraOAuth2LoginConfig.getAuthorizationGrantType()));
		oauth2Client.setResponseTypes(Arrays.asList("token","code","id_token"));
		oauth2Client.setScope(StringUtils.join(hydraOAuth2LoginConfig.getScope(), " "));
		oauth2Client.setRedirectUris(Arrays.asList(hydraOAuth2LoginConfig.getRedirectUri()));
		oauth2Client.setTokenEndpointAuthMethod("client_secret_basic");
		hydraAdminApi.createOAuth2Client(oauth2Client);
		LOGGER.info(">>> 创建Client({})成功!", hydraOAuth2LoginConfig.getClientId());
	}
	
}
