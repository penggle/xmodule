package com.penglecode.xmodule.common.security.oauth2.client.servlet.support;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientId;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.util.Assert;

/**
 * 自定义的OAuth2AuthorizedClientRepository，用来处理匿名用户的OAuth2AuthorizedClient获取问题：
 * 例如匿名情况下,例如用户登录(通过OAuth2 Password模式),出现不同用户名登录时返回的OAuth2AuthorizedClient是同一个的问题(即返回了同样的令牌值)
 * 
 * @author 	pengpeng
 * @date 	2020年1月31日 下午10:13:31
 */
public class HttpSessionOAuth2AuthorizedClientRepository implements OAuth2AuthorizedClientRepository {

	private static final String DEFAULT_AUTHORIZED_CLIENTS_ATTR_NAME =
			HttpSessionOAuth2AuthorizedClientRepository.class.getName() +  ".AUTHORIZED_CLIENTS";
	private final String sessionAttributeName = DEFAULT_AUTHORIZED_CLIENTS_ATTR_NAME;

	@SuppressWarnings("unchecked")
	@Override
	public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, Authentication principal,
																		HttpServletRequest request) {
		Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
		Assert.notNull(request, "request cannot be null");
		return (T) this.getAuthorizedClients(request).get(new OAuth2AuthorizedClientId(clientRegistrationId, principal.getName()));
	}

	@Override
	public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal,
										HttpServletRequest request, HttpServletResponse response) {
		Assert.notNull(authorizedClient, "authorizedClient cannot be null");
		Assert.notNull(request, "request cannot be null");
		Assert.notNull(response, "response cannot be null");
		Map<OAuth2AuthorizedClientId, OAuth2AuthorizedClient> authorizedClients = this.getAuthorizedClients(request);
		authorizedClients.put(new OAuth2AuthorizedClientId(authorizedClient.getClientRegistration().getRegistrationId(), principal.getName()), authorizedClient);
		request.getSession().setAttribute(this.sessionAttributeName, authorizedClients);
	}

	@Override
	public void removeAuthorizedClient(String clientRegistrationId, Authentication principal,
										HttpServletRequest request, HttpServletResponse response) {
		Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
		Assert.notNull(request, "request cannot be null");
		Map<OAuth2AuthorizedClientId, OAuth2AuthorizedClient> authorizedClients = this.getAuthorizedClients(request);
		if (!authorizedClients.isEmpty()) {
			if (authorizedClients.remove(new OAuth2AuthorizedClientId(clientRegistrationId, principal.getName())) != null) {
				if (!authorizedClients.isEmpty()) {
					request.getSession().setAttribute(this.sessionAttributeName, authorizedClients);
				} else {
					request.getSession().removeAttribute(this.sessionAttributeName);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Map<OAuth2AuthorizedClientId, OAuth2AuthorizedClient> getAuthorizedClients(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Map<OAuth2AuthorizedClientId, OAuth2AuthorizedClient> authorizedClients = session == null ? null :
				(Map<OAuth2AuthorizedClientId, OAuth2AuthorizedClient>) session.getAttribute(this.sessionAttributeName);
		if (authorizedClients == null) {
			authorizedClients = new HashMap<>();
		}
		return authorizedClients;
	}

}
