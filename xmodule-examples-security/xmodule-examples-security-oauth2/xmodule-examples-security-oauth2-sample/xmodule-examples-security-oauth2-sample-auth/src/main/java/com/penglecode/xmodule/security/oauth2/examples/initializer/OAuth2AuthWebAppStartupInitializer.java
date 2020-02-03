package com.penglecode.xmodule.security.oauth2.examples.initializer;

import java.util.Arrays;
import java.util.List;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.RolesRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import com.penglecode.xmodule.common.initializer.SpringWebAppStartupInitializer;
import com.penglecode.xmodule.security.oauth2.examples.config.KeycloakOAuth2ConfigProperties;

@Component
public class OAuth2AuthWebAppStartupInitializer implements SpringWebAppStartupInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2AuthWebAppStartupInitializer.class);

	@Autowired
	private Keycloak keycloak;
	
	@Autowired
	private KeycloakOAuth2ConfigProperties keycloakOAuth2Config;
	
	@Override
	public void initialize(ConfigurableWebApplicationContext applicationContext) throws Exception {
		LOGGER.info(">>> 初始化Keycloak realm: {}", keycloakOAuth2Config.getRealm());
		createRealmIfNotExists(applicationContext);
	}
	
	/**
	 * 创建应用Realm如果不存在
	 */
	protected void createRealmIfNotExists(ConfigurableWebApplicationContext applicationContext) {
		List<RealmRepresentation> allRealms = keycloak.realms().findAll();
		if(!CollectionUtils.isEmpty(allRealms)) {
			boolean notExists = allRealms.stream().noneMatch(r -> r.getRealm().equals(keycloakOAuth2Config.getRealm()));
			if(notExists) {
				RealmRepresentation realm = new RealmRepresentation();
				realm.setId(keycloakOAuth2Config.getRealm()); //如果不设ID则或自动生成UUID填充
				realm.setRealm(keycloakOAuth2Config.getRealm()); //realm名称应该唯一
				realm.setEnabled(true);
				realm.setDisplayName("Spring Security OAuth2 示例");
				realm.setDisplayNameHtml("<h3>Spring Security OAuth2 示例</h3>");
				realm.setAccessTokenLifespan(applicationContext.getEnvironment().getProperty("server.servlet.session.timeout", Integer.class, 7200)); //设置access_token过期时间(秒),此设置还可以在具体的Client那里进行覆盖
				realm.setSsoSessionIdleTimeout(60 * 60 * 24 * 7); //设置refresh_token过期时间(秒)
				realm.setSsoSessionMaxLifespan(60 * 60 * 24 * 7); //设置access_token过期的最大时间
				//添加应用角色
				RolesRepresentation roles = new RolesRepresentation();
				RoleRepresentation role1 = new RoleRepresentation();
				role1.setName("customer");
				role1.setDescription("customer");
				role1.setClientRole(false);
				role1.setComposite(false);
				roles.setRealm(Arrays.asList(role1));
				realm.setRoles(roles);
				//设置默认角色(即用户的默认角色)
				realm.setDefaultRoles(Arrays.asList("customer"));
				keycloak.realms().create(realm);
				LOGGER.info(">>> 创建Realm({})成功!", realm.getRealm());
			}
		}
	}

}
