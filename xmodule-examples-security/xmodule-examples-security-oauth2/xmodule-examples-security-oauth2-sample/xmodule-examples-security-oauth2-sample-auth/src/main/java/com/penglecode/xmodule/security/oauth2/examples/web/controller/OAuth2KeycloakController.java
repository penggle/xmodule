package com.penglecode.xmodule.security.oauth2.examples.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientScopesResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.ProtocolMappersResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.ClientScopeRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.ProtocolMapperRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
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

import com.penglecode.xmodule.common.security.oauth2.consts.OAuth2ApplicationConstants;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.servlet.support.HttpApiResourceSupport;
import com.penglecode.xmodule.security.oauth2.common.model.OAuth2LoginUser;
import com.penglecode.xmodule.security.oauth2.examples.config.KeycloakOAuth2ClientProperties;
import com.penglecode.xmodule.security.oauth2.examples.config.KeycloakOAuth2ConfigProperties;

@RestController
@RequestMapping("/api/keycloak")
public class OAuth2KeycloakController extends HttpApiResourceSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2KeycloakController.class);
	
	@Autowired
	private Keycloak keycloak;
	
	@Autowired
	private KeycloakOAuth2ConfigProperties keycloakOAuth2Config;
	
	@GetMapping(value="/init", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> init() {
		LOGGER.info(">>> 重新初始化Keycloak服务端配置开始...");
		createClientsAndScopes();
		createRoles();
		createUsers();
		return Result.success().build();
	}
	
	/**
	 * 先删除后创建Client
	 */
	protected void createClientsAndScopes() {
		List<KeycloakOAuth2ClientProperties> oauth2Clients = Arrays.asList(keycloakOAuth2Config.getPasswordClient(), keycloakOAuth2Config.getClientcredsClient());
		
		Set<String> clientScopes = new HashSet<String>();
		for(KeycloakOAuth2ClientProperties oauth2Client : oauth2Clients) {
			clientScopes.addAll(Arrays.asList(oauth2Client.getScope())); //collect and distinct all scopes
		}
		
		ClientScopesResource clientScopesResource = keycloak.realm(keycloakOAuth2Config.getRealm()).clientScopes();
		List<ClientScopeRepresentation> allClientScopes = clientScopesResource.findAll();
		for(String scope : clientScopes) {
			boolean notExists = allClientScopes.stream().noneMatch(s -> s.getName().equals(scope));
			if(notExists) {
				ClientScopeRepresentation clientScope = new ClientScopeRepresentation();
				clientScope.setName(scope);
				clientScope.setProtocol("openid-connect");
				clientScopesResource.create(clientScope);
				LOGGER.info(">>> 创建ClientScope({})成功!", clientScope.getName());
			}
		}
		
		ClientsResource clientsResource = keycloak.realm(keycloakOAuth2Config.getRealm()).clients();
		for(KeycloakOAuth2ClientProperties oauth2Client : oauth2Clients) {
			clientsResource.findByClientId(oauth2Client.getClientId()).forEach(c -> {
				clientsResource.get(c.getId()).remove();
				LOGGER.info(">>> 删除Client({})成功!", c.getClientId()); 
			});
			
			ClientRepresentation client = new ClientRepresentation();
			client.setId(UUID.randomUUID().toString());
			client.setClientId(oauth2Client.getClientId());
			client.setName(oauth2Client.getClientId());
			client.setProtocol("openid-connect");
			client.setBearerOnly(false);
			client.setPublicClient(false);
			client.setConsentRequired("authorization_code".equals(oauth2Client.getAuthorizationGrantType())); //是否用户需手动“同意”?
			client.setEnabled(true);
			client.setClientAuthenticatorType("client-secret");
			client.setSecret(oauth2Client.getClientSecret());
			client.setDescription(String.format("基于spring-security-oauth2-client的OAuth2(%s)客户端", oauth2Client.getAuthorizationGrantType()));
			//支持使用授权码进行标准的基于OpenID连接重定向的身份验证,例如OpenID Connect或者OAuth2授权码模式(Authorization Code)
			client.setStandardFlowEnabled(true);
			//支持直接访问授权,这意味着客户端可以访问用户的用户名/密码，并直接与Keycloak服务器交换访问令牌,例如OAuth2密码模式(Resource Owner Password Credentials)
			client.setDirectAccessGrantsEnabled(true);
			//允许您对此客户端进行身份验证，以密钥隐藏和检索专用于此客户端的访问令牌,例如OAuth2客户端模式(Client Credentials)
			client.setServiceAccountsEnabled(true);
			client.setWebOrigins(Arrays.asList("*"));
			client.setBaseUrl(keycloakOAuth2Config.getBaseUrl());
			client.setRedirectUris(Arrays.asList(keycloakOAuth2Config.getBaseUrl() + "/*"));
			//为客户端启用/禁用细粒度授权支持
			ResourceServerRepresentation authorizationSettings = new ResourceServerRepresentation();
			client.setAuthorizationSettings(authorizationSettings);
			
			Map<String,String> attributes = new HashMap<String,String>();
			
			//设置默认的ClientScope
			client.setDefaultClientScopes(Arrays.asList(oauth2Client.getScope()));
			
			//Authorization Code/Password模式的Client需要设置Client的Mappers，定制AccessToken中的返回字段(增加username、user_roles字段)
			if(oauth2Client.getAuthorizationGrantType().equals("authorization_code") || oauth2Client.getAuthorizationGrantType().equals("password")) {
				//设置access_token的寿命(秒)
				attributes.put("access.token.lifespan", String.valueOf(OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getUserTokenTimeout().getSeconds()));
				
				List<ProtocolMapperRepresentation> mappers = new ArrayList<ProtocolMapperRepresentation>();
				ProtocolMapperRepresentation mapper = null;
				
				/**
				 * 设置AccessToken中的sub字段,其值与#OAuth2PrincipalNameAuthentication.name及#JwtAuthenticationToken.name的值保持一致(至于为什么要保持一致，究其原因是JwtAuthenticationToken.name硬编码取自令牌的sub字段),
				 * 这样才能保证#OAuth2AuthorizedClientRepository.loadAuthorizedClient()方法在查找OAuth2AuthorizedClient时才能获取正确的结果
				 * (因为OAuth2AuthorizedClientRepository.loadAuthorizedClient()方法查找逻辑是根据key(clientRegistrationId + principal.name)作为唯一键来查找的)
				 */
				mapper = createUserPropertyMapper("Subject", "username", "sub", true, true, true, "String");
				mappers.add(mapper);
				
				mapper = createUserPropertyMapper("User Name", "username", "username", true, true, true, "String");
				mappers.add(mapper);
				
				mapper = createUserRealmRoleMapper("User Roles", "userRoles", true, true, true);
				mappers.add(mapper);
				
				mapper = createUserAttributeMapper("User ID", "userId", "userId", true, true, true, "long");
				mappers.add(mapper);
				mapper = createUserAttributeMapper("User Type", "userType", "userType", false, false, true, "int");
				mappers.add(mapper);
				mapper = createUserAttributeMapper("User Email", "email", "email", false, false, true, "String");
				mappers.add(mapper);
				mapper = createUserAttributeMapper("User MobilePhone", "mobilePhone", "mobilePhone", false, false, true, "String");
				mappers.add(mapper);
				mapper = createUserAttributeMapper("User Enabled", "enabled", "enabled", false, false, true, "String");
				mappers.add(mapper);
				mapper = createUserAttributeMapper("User LoginTimes", "loginTimes", "loginTimes", false, false, true, "int");
				mappers.add(mapper);
				mapper = createUserAttributeMapper("User LastLoginTime", "lastLoginTime", "lastLoginTime", false, false, true, "String");
				mappers.add(mapper);
				mapper = createUserAttributeMapper("User CreateTime", "createTime", "createTime", false, false, true, "String");
				mappers.add(mapper);
				mapper = createUserAttributeMapper("User UpdateTime", "updateTime", "updateTime", false, false, true, "String");
				mappers.add(mapper);
				
				client.setProtocolMappers(mappers);
			} else if (oauth2Client.getAuthorizationGrantType().equals("client_credentials")) { //client_credentials模式的Client需要设置Client的Mappers，定制AccessToken中的返回字段(增加appId等字段)
				//设置access_token的寿命(秒)
				attributes.put("access.token.lifespan", String.valueOf(OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getAppTokenTimeout().getSeconds()));
				attributes.put("appId", "1");
				attributes.put("appName", getEnvironment().getProperty("spring.application.name"));
				
				List<ProtocolMapperRepresentation> mappers = new ArrayList<ProtocolMapperRepresentation>();
				ProtocolMapperRepresentation mapper = null;
				
				/**
				 * 设置AccessToken中的sub字段,其值与#OAuth2PrincipalNameAuthentication.name及#JwtAuthenticationToken.name的值保持一致(至于为什么要保持一致，究其原因是JwtAuthenticationToken.name硬编码取自令牌的sub字段),
				 * 这样才能保证#OAuth2AuthorizedClientRepository.loadAuthorizedClient()方法在查找OAuth2AuthorizedClient时才能获取正确的结果
				 * (因为OAuth2AuthorizedClientRepository.loadAuthorizedClient()方法查找逻辑是根据key(clientRegistrationId + principal.name)作为唯一键来查找的)
				 */
				mapper = createUserSessionNoteMapper("Subject", "clientId", "sub", true, true, "String");
				mappers.add(mapper);
				
				mapper = createUserAttributeMapper("APP ID", "appId", "appId", true, true, true, "long");
				mappers.add(mapper);
				
				mapper = createUserAttributeMapper("APP Name", "appName", "appName", true, true, true, "String");
				mappers.add(mapper);
				
				client.setProtocolMappers(mappers);
			}
			
			client.setAttributes(attributes);
			
			clientsResource.create(client);
			LOGGER.info(">>> 创建Client({})成功!", client.getClientId());
			
			//配置客户端的[Client ID, Client Host, Client IP Address]Mappers，以达到jwt令牌瘦身的目的
			ProtocolMappersResource mapperResource = clientsResource.get(client.getId()).getProtocolMappers();
			List<ProtocolMapperRepresentation> mappers = mapperResource.getMappers();
			if(!CollectionUtils.isEmpty(mappers)) {
				mappers.forEach(m -> {
					if(m.getName().equals("Client ID") || m.getName().equals("Client Host") || m.getName().equals("Client IP Address")) {
						m.getConfig().put("access.token.claim", "false"); //令牌瘦身
						mapperResource.update(m.getId(), m);
						LOGGER.info(">>> 更新Client({})的Mapper({})!", client.getClientId(), m.getName());
					}
				});
			}
		}
	}
	
	/**
	 * 创建User Realm Role Mapper
	 * 对应Keycloak Admin界面上 Clients -> Select a client -> Mappers -> Create -> 'Mapper Type' select 'User Realm Role' option
	 */
	protected ProtocolMapperRepresentation createUserRealmRoleMapper(String mapperName, String claimName, boolean addToIdToken, boolean addToAccessToken, boolean addToUserInfo) {
		ProtocolMapperRepresentation mapper = new ProtocolMapperRepresentation();
		mapper.setName(mapperName);
		mapper.setProtocol("openid-connect");
		mapper.setProtocolMapper("oidc-usermodel-realm-role-mapper");
		
		Map<String,String> config = new LinkedHashMap<String,String>();
		config.put("userinfo.token.claim", String.valueOf(addToUserInfo)); //添加到userinfo中
		config.put("id.token.claim", String.valueOf(addToIdToken)); //添加到IdToken中
		config.put("access.token.claim", String.valueOf(addToAccessToken)); //添加到AccessToken中
		config.put("claim.name", claimName);
		config.put("jsonType.label", "String");
		config.put("multivalued", "true");
		mapper.setConfig(config);
		return mapper;
	}
	
	/**
	 * 创建User Property Mapper
	 * 对应Keycloak Admin界面上 Clients -> Select a client -> Mappers -> Create -> 'Mapper Type' select 'User Property' option
	 */
	protected ProtocolMapperRepresentation createUserPropertyMapper(String mapperName, String userAttrName, String claimName, boolean addToIdToken, boolean addToAccessToken, boolean addToUserInfo, String valueType) {
		ProtocolMapperRepresentation mapper = new ProtocolMapperRepresentation();
		mapper.setName(mapperName);
		mapper.setProtocol("openid-connect");
		mapper.setProtocolMapper("oidc-usermodel-property-mapper");
		
		Map<String,String> config = new LinkedHashMap<String,String>();
		config.put("userinfo.token.claim", String.valueOf(addToUserInfo)); //添加到userinfo中
		config.put("user.attribute", userAttrName);
		config.put("id.token.claim", String.valueOf(addToIdToken)); //添加到IdToken中
		config.put("access.token.claim", String.valueOf(addToAccessToken)); //添加到AccessToken中
		config.put("claim.name", claimName);
		config.put("jsonType.label", StringUtils.defaultIfEmpty(valueType, "String"));
		mapper.setConfig(config);
		return mapper;
	}
	
	/**
	 * 创建User Session Note Mapper
	 * 对应Keycloak Admin界面上 Clients -> Select a client -> Mappers -> Create -> 'Mapper Type' select 'User Session Note' option
	 */
	protected ProtocolMapperRepresentation createUserSessionNoteMapper(String mapperName, String noteName, String claimName, boolean addToIdToken, boolean addToAccessToken, String valueType) {
		ProtocolMapperRepresentation mapper = new ProtocolMapperRepresentation();
		mapper.setName(mapperName);
		mapper.setProtocol("openid-connect");
		mapper.setProtocolMapper("oidc-usersessionmodel-note-mapper");
		
		//https://www.keycloak.org/docs/latest/server_admin/index.html#_protocol-mappers_oidc-user-session-note-mappers
		Map<String,String> config = new LinkedHashMap<String,String>();
		config.put("user.session.note", noteName); //
		config.put("id.token.claim", String.valueOf(addToIdToken)); //添加到IdToken中
		config.put("access.token.claim", String.valueOf(addToAccessToken)); //添加到AccessToken中
		config.put("claim.name", claimName);
		config.put("jsonType.label", StringUtils.defaultIfEmpty(valueType, "String"));
		mapper.setConfig(config);
		return mapper;
	}
	
	/**
	 * 创建User Attribute Mapper
	 * 对应Keycloak Admin界面上 Clients -> Select a client -> Mappers -> Create -> 'Mapper Type' select 'User Attribute' option
	 * @return
	 */
	protected ProtocolMapperRepresentation createUserAttributeMapper(String mapperName, String userAttrName, String claimName, boolean addToIdToken, boolean addToAccessToken, boolean addToUserInfo, String valueType) {
		ProtocolMapperRepresentation mapper = new ProtocolMapperRepresentation();
		mapper.setName(mapperName);
		mapper.setProtocol("openid-connect");
		mapper.setProtocolMapper("oidc-usermodel-attribute-mapper");
		
		Map<String,String> config = new LinkedHashMap<String,String>();
		config.put("userinfo.token.claim", String.valueOf(addToUserInfo)); //添加到userinfo中
		config.put("user.attribute", userAttrName);
		config.put("id.token.claim", String.valueOf(addToIdToken)); //添加到IdToken中
		config.put("access.token.claim", String.valueOf(addToAccessToken)); //添加到AccessToken中
		config.put("claim.name", claimName);
		config.put("jsonType.label", StringUtils.defaultIfEmpty(valueType, "String"));
		mapper.setConfig(config);
		return mapper;
	}
	
	/**
	 * 先删除后创建Role
	 */
	protected void createRoles() {
		String roleName = "customer";
		RolesResource rolesResource = keycloak.realm(keycloakOAuth2Config.getRealm()).roles();
		boolean notExists = rolesResource.list().stream().noneMatch(r -> r.getName().equals(roleName));
		if(notExists) {
			RoleRepresentation role = new RoleRepresentation();
			role.setName(roleName);
			role.setComposite(false);
			role.setClientRole(false);
			role.setDescription(roleName);
			rolesResource.create(role);
		}
	}
	
	/**
	 * 先删除后创建User
	 */
	protected void createUsers() {
		UsersResource usersResource = keycloak.realm(keycloakOAuth2Config.getRealm()).users();
		RolesResource rolesResource = keycloak.realm(keycloakOAuth2Config.getRealm()).roles();
		for(OAuth2LoginUser loginUser : keycloakOAuth2Config.getUsers()) {
			usersResource.search(loginUser.getUsername()).forEach(u -> {
				usersResource.delete(u.getId());
				LOGGER.info(">>> 删除User({})成功!", u.getUsername()); 
			});
			
			List<String> userRoles = Arrays.asList("customer");
			
			UserRepresentation user = new UserRepresentation();
			user.setUsername(loginUser.getUsername());
			user.setEmail(loginUser.getEmail());
			user.setEnabled(loginUser.getEnabled());
			user.setAttributes(loginUser.getKcUserAttributes()); //设置User Attributes
			CredentialRepresentation credential = new CredentialRepresentation();
			credential.setTemporary(false);
			credential.setType(CredentialRepresentation.PASSWORD);
			credential.setValue(loginUser.getPassword());
			user.setCredentials(Arrays.asList(credential));
			usersResource.create(user);
			LOGGER.info(">>> 创建User({})成功!", user.getUsername());
			
			//删除默认的offline_access和uma_authorization,再添加userRoles中的
			usersResource.search(loginUser.getUsername()).forEach(u -> {
				RoleScopeResource rsResource = usersResource.get(u.getId()).roles().realmLevel();
				rsResource.remove(rsResource.listEffective());
				List<RoleRepresentation> rolesToAdd = rolesResource.list().stream().filter(r -> userRoles.contains(r.getName())).collect(Collectors.toList());
				rsResource.add(rolesToAdd);
			});
		}
	}
	
}
