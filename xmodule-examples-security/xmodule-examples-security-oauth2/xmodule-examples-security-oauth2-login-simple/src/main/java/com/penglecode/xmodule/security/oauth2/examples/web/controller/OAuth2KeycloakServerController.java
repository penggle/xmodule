package com.penglecode.xmodule.security.oauth2.examples.web.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.ProtocolMappersResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.ProtocolMapperRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.ResourceServerRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.security.oauth2.examples.config.KeycloakOAuth2LoginConfigProperties;

@RestController
@RequestMapping("/api/keycloak")
public class OAuth2KeycloakServerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2KeycloakServerController.class);
	
	@Autowired
	private Keycloak keycloak;
	
	@Autowired
	private KeycloakOAuth2LoginConfigProperties keycloakOAuth2LoginConfig;
	
	@GetMapping(value="/init", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> init() {
		LOGGER.info(">>> 重新初始化Keycloak服务端配置开始...");
		removeRealm();
		createRealm();
		removeClient();
		createClient();
		removeUser();
		createUser();
		return Result.success().build();
	}
	
	/**
	 * 删除Realm(如果存在的话)
	 */
	protected void removeRealm() {
		List<RealmRepresentation> allRealms = keycloak.realms().findAll();
		if(!CollectionUtils.isEmpty(allRealms)) {
			allRealms.stream().filter(r -> r.getRealm().equals(keycloakOAuth2LoginConfig.getRealm())).findFirst().ifPresent(r -> {
				RealmResource realmResource = keycloak.realm(r.getRealm());
				realmResource.remove();
				LOGGER.info(">>> 删除Realm({})成功!", r.getRealm());
			});
		}
	}
	
	/**
	 * 创建Realm
	 */
	protected void createRealm() {
		RealmRepresentation realm = new RealmRepresentation();
		realm.setId(keycloakOAuth2LoginConfig.getRealm()); //如果不设ID则或自动生成UUID填充
		realm.setRealm(keycloakOAuth2LoginConfig.getRealm()); //realm名称应该唯一
		realm.setEnabled(true);
		realm.setDisplayName("Spring Security OAuth2 示例");
		realm.setDisplayNameHtml("<h3>Spring Security OAuth2 示例</h3>");
		realm.setAccessTokenLifespan(30 * 60); //设置access_token过期时间(秒),此设置还可以在具体的Client那里进行覆盖
		realm.setSsoSessionIdleTimeout(60 * 60 * 24); //设置refresh_token过期时间(秒)
		keycloak.realms().create(realm);
		LOGGER.info(">>> 创建Realm({})成功!", realm.getRealm());
	}
	
	/**
	 * 删除Client
	 */
	protected void removeClient() {
		ClientsResource clientsResource = keycloak.realm(keycloakOAuth2LoginConfig.getRealm()).clients();
		clientsResource.findByClientId(keycloakOAuth2LoginConfig.getClientId()).forEach(c -> {
			clientsResource.get(c.getId()).remove();
			LOGGER.info(">>> 删除Client({})成功!", c.getClientId()); 
		});
	}
	
	/**
	 * 创建Client
	 */
	protected void createClient() {
		ClientsResource clientsResource = keycloak.realm(keycloakOAuth2LoginConfig.getRealm()).clients();
		
		ClientRepresentation client = new ClientRepresentation();
		client.setId(UUID.randomUUID().toString());
		client.setClientId(keycloakOAuth2LoginConfig.getClientId());
		client.setName(keycloakOAuth2LoginConfig.getClientName());
		client.setProtocol("openid-connect");
		client.setBearerOnly(false);
		client.setPublicClient(false);
		client.setConsentRequired(true); //用户需手动“同意”
		client.setEnabled(true);
		client.setClientAuthenticatorType("client-secret");
		client.setSecret(keycloakOAuth2LoginConfig.getClientSecret());
		client.setDescription("基于spring-security-oauth2-client的OAuth2Login客户端");
		//支持使用授权码进行标准的基于OpenID连接重定向的身份验证,例如OpenID Connect或者OAuth2授权码模式(Authorization Code)
		client.setStandardFlowEnabled(true);
		//支持直接访问授权,这意味着客户端可以访问用户的用户名/密码，并直接与Keycloak服务器交换访问令牌,例如OAuth2密码模式(Resource Owner Password Credentials)
		client.setDirectAccessGrantsEnabled(true);
		//允许您对此客户端进行身份验证，以密钥隐藏和检索专用于此客户端的访问令牌,例如OAuth2客户端模式(Client Credentials)
		client.setServiceAccountsEnabled(true);
		client.setWebOrigins(Arrays.asList("*"));
		client.setBaseUrl(keycloakOAuth2LoginConfig.getBaseUrl());
		client.setRedirectUris(Arrays.asList(keycloakOAuth2LoginConfig.getBaseUrl() + "/*"));
		//为客户端启用/禁用细粒度授权支持
		ResourceServerRepresentation authorizationSettings = new ResourceServerRepresentation();
		client.setAuthorizationSettings(authorizationSettings);
		
		Map<String,String> attributes = new HashMap<String,String>();
		//设置access_token的寿命(秒)
		attributes.put("access.token.lifespan", String.valueOf(30 * 60));
		client.setAttributes(attributes);
		
		//设置默认的ClientScope
		client.setDefaultClientScopes(Arrays.asList(keycloakOAuth2LoginConfig.getScope()));
		
		clientsResource.create(client);
		LOGGER.info(">>> 创建Client({})成功!", client.getClientId());
		
		//配置客户端的[Client ID, Client Host, Client IP Address]Mappers，以达到jwt令牌瘦身的目的
		ProtocolMappersResource mapperResource = clientsResource.get(client.getId()).getProtocolMappers();
		List<ProtocolMapperRepresentation> mappers = mapperResource.getMappers();
		if(!CollectionUtils.isEmpty(mappers)) {
			mappers.forEach(m -> {
				if(m.getName().equals("Client ID") || m.getName().equals("Client Host") || m.getName().equals("Client IP Address")) {
					m.getConfig().put("access.token.claim", "false");
					mapperResource.update(m.getId(), m);
					LOGGER.info(">>> 更新Client({})的Mapper({})!", client.getClientId(), m.getName());
				}
			});
		}
	}
	
	protected void removeUser() {
		UsersResource usersResource = keycloak.realm(keycloakOAuth2LoginConfig.getRealm()).users();
		usersResource.search(keycloakOAuth2LoginConfig.getLoginUsername()).forEach(u -> {
			usersResource.delete(u.getId());
			LOGGER.info(">>> 删除User({})成功!", u.getUsername()); 
		});
	}
	
	protected void createUser() {
		UsersResource usersResource = keycloak.realm(keycloakOAuth2LoginConfig.getRealm()).users();
		UserRepresentation user = new UserRepresentation();
		user.setUsername(keycloakOAuth2LoginConfig.getLoginUsername());
		user.setFirstName("peng");
		user.setLastName("san");
		user.setEmail("pengsan@qq.com");
		user.setEnabled(true);
		CredentialRepresentation credential = new CredentialRepresentation();
		credential.setTemporary(false);
		credential.setType(CredentialRepresentation.PASSWORD);
		credential.setValue(keycloakOAuth2LoginConfig.getLoginPassword());
		user.setCredentials(Arrays.asList(credential));
		usersResource.create(user);
		LOGGER.info(">>> 创建User({})成功!", keycloakOAuth2LoginConfig.getLoginUsername()); 
	}
	
}
