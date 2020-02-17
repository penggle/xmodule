package com.penglecode.xmodule.security.oauth2.examples.web.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.penglecode.xmodule.common.security.oauth2.client.servlet.util.OAuth2ClientUtils;
import com.penglecode.xmodule.common.security.oauth2.client.servlet.util.OAuth2LoginUtils;
import com.penglecode.xmodule.common.security.servlet.util.SpringSecurityUtils;
import com.penglecode.xmodule.common.util.ExceptionUtils;
import com.penglecode.xmodule.common.util.JsonUtils;

@Controller
public class OAuth2LoginController implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2LoginController.class);

	private final Map<String,String> oauth2LoginLinks = new LinkedHashMap<String,String>();
	
	@GetMapping(value="/login")
	public String login(Model model) {
		model.addAttribute("oauth2LoginLinks", oauth2LoginLinks);
		return "login";
	}
	
	@RequestMapping(value="/login/failure")
	public String loginFailure(HttpServletRequest request, HttpServletResponse response, Model model) {
		Exception exception = SpringSecurityUtils.getAuthenticationException(request);
		LOGGER.error(String.format(">>> login failure: %s", exception.getMessage()), exception);
		model.addAttribute("error", Boolean.TRUE);
		model.addAttribute("message", ExceptionUtils.getRootCauseMessage(exception));
		return "login";
	}
	
	@RequestMapping(value="/login/success")
	public String loginSuccess() {
		return "redirect:/index";
	}
	
	@RequestMapping(value="/index")
	public String index(Model model, OAuth2AuthenticationToken authentication) {
		Map<String,Object> userInfo = null;
		LOGGER.info(">>> authentication = {}", authentication);
		LOGGER.info(">>> OAuth2 User Login Success!");
		OAuth2AuthenticationToken authentication0 = SpringSecurityUtils.getCurrentAuthentication();
		System.out.println(authentication0 == authentication);
		userInfo = OAuth2LoginUtils.getCurrentOAuth2UserInfo();
		OAuth2AuthorizedClient oauth2AuthorizedClient = OAuth2ClientUtils.getOAuth2AuthorizedClient(authentication0.getAuthorizedClientRegistrationId(), authentication0, null);
		OAuth2AccessToken accessToken = oauth2AuthorizedClient.getAccessToken();
		LOGGER.debug(">>> userInfo = {}", JsonUtils.object2Json(userInfo));
		LOGGER.debug(">>> accessToken = {}", JsonUtils.object2Json(accessToken));
		
		model.addAttribute("userInfo", userInfo);
		return "index";
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String,String> loginLinks = OAuth2LoginUtils.getConfiguredOAuth2LoginLinks();
		if(loginLinks != null) {
			oauth2LoginLinks.putAll(loginLinks);
		}
	}

}
