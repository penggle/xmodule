package com.penglecode.xmodule.security.oauth2.examples.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.security.oauth2.client.servlet.util.OAuth2ClientUtils;
import com.penglecode.xmodule.common.security.oauth2.consts.OAuth2ApplicationConstants;
import com.penglecode.xmodule.common.security.servlet.util.SpringSecurityUtils;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.servlet.support.HttpApiResourceSupport;

/**
 * 基于OAuth2 password模式的用户登录
 * 
 * @author 	pengpeng
 * @date 	2020年2月3日 下午10:08:59
 */
@RestController
@RequestMapping("/api/oauth2")
public class OAuth2AuthApiController extends HttpApiResourceSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2AuthApiController.class);

	@Autowired
	private OAuth2AuthorizedClientManager authorizedClientManager;
	
	@Autowired
	private DefaultOAuth2UserService defaultOAuth2UserService;
	
	/**
	 * 基于OAuth2(Password模式)的用户登录
	 * (该登录接口重复调用时，只有第一次会去OAuth2认证服务器获取一个新的令牌，
	 *  后续录接请求如果当前令牌仍然有效的话则直接返回当前令牌)
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping(value="/login", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> login(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info(">>> 执行OAuth2(Password模式)用户登录...");
		ClientRegistration clientRegistration = OAuth2ClientUtils.getClientRegistrationById(OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getUserRegistrationId());
		Authentication authentication = SpringSecurityUtils.getCurrentAuthentication(); //初次登录时应该是个匿名的Authentication实例
		authentication = OAuth2ClientUtils.preparePasswordOAuth2Authentication(clientRegistration, authentication, request.getParameter(OAuth2ParameterNames.USERNAME)); //创建执行OAuth2 Password模式登录的Authentication
		OAuth2AuthorizeRequest.Builder builder = OAuth2AuthorizeRequest.withClientRegistrationId(clientRegistration.getRegistrationId()).principal(authentication);
		builder.attributes(attributes -> {
			attributes.put(HttpServletRequest.class.getName(), request);
			attributes.put(HttpServletResponse.class.getName(), response);
		});
		OAuth2AuthorizeRequest authorizeRequest = builder.build();
		OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);
		Map<String,Object> oauth2Tokens = new HashMap<String,Object>();
		oauth2Tokens.put("accessToken", authorizedClient.getAccessToken());
		oauth2Tokens.put("refreshToken", authorizedClient.getRefreshToken());
		return Result.success().data(oauth2Tokens).build();
	}
	
	/**
	 * 基于OAuth2(Password模式)的用户登录续约
	 * (续约即：通过refresh_token获取一个新的access_token替换即将过期的旧access_token，
	 *  需要说明的是续约请求是有条件的，即有个全局提前续约时间的设定(见#OAuth2AuthorizedClientProvider的各个实现类的clockSkew字段设定)，
	 *  只有令牌剩余有效时间小于clockSkew时才能续约得到新的令牌，否则仍然返回旧的令牌)
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping(value="/renewal", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> renewal(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info(">>> 执行OAuth2(Password模式)用户登录续约...");
		ClientRegistration clientRegistration = OAuth2ClientUtils.getClientRegistrationById(OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getUserRegistrationId());
		Authentication authentication = SpringSecurityUtils.getCurrentAuthentication();
		OAuth2AuthorizedClient oauth2AuthorizedClient = OAuth2ClientUtils.getOAuth2AuthorizedClient(clientRegistration.getRegistrationId(), authentication, request);
		
		OAuth2AuthorizeRequest.Builder builder = null;
		if(oauth2AuthorizedClient != null) {
			//构造欺骗令牌，达到强制立即刷新令牌的目的
			OAuth2AuthorizedClient trickAuthorizedClient = OAuth2ClientUtils.createTrickOAuth2AuthorizedClient(oauth2AuthorizedClient);
			builder = OAuth2AuthorizeRequest.withAuthorizedClient(trickAuthorizedClient).principal(authentication);
		} else {
			builder = OAuth2AuthorizeRequest.withClientRegistrationId(clientRegistration.getRegistrationId()).principal(authentication);
		}
		builder.attributes(attributes -> {
			attributes.put(HttpServletRequest.class.getName(), request);
			attributes.put(HttpServletResponse.class.getName(), response);
			attributes.put(OAuth2AuthorizationContext.REQUEST_SCOPE_ATTRIBUTE_NAME, clientRegistration.getScopes().toArray(new String[0]));
		});
		OAuth2AuthorizeRequest authorizeRequest = builder.build();
		OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);
		Map<String,Object> oauth2Tokens = new HashMap<String,Object>();
		oauth2Tokens.put("accessToken", authorizedClient.getAccessToken());
		oauth2Tokens.put("refreshToken", authorizedClient.getRefreshToken());
		return Result.success().data(oauth2Tokens).build();
	}
	
	/**
	 * 获取登录用户信息
	 * @return
	 */
	@GetMapping(value="/userinfo", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> getAuthenticatedUserInfo(JwtAuthenticationToken authentication) {
		Authentication authentication0 = SpringSecurityUtils.getCurrentAuthentication();
		System.out.println(authentication == authentication0);
		LOGGER.info(">>> 获取登录用户信息，authentication = {}", authentication);
		
		ClientRegistration clientRegistration = OAuth2ClientUtils.getClientRegistrationById(OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getUserRegistrationId());
		OAuth2AuthorizedClient oauth2AuthorizedClient = OAuth2ClientUtils.getOAuth2AuthorizedClient(clientRegistration.getRegistrationId(), authentication, null);
		OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, oauth2AuthorizedClient.getAccessToken());
		
		OAuth2User oauth2User = defaultOAuth2UserService.loadUser(userRequest);
		return Result.success().data(oauth2User).build();
	}

}
