package com.penglecode.xmodule.hydra.oauth2.examples.web.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.penglecode.xmodule.common.security.servlet.util.SpringSecurityUtils;
import com.penglecode.xmodule.common.util.ExceptionUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.servlet.support.HttpApiResourceSupport;
import com.penglecode.xmodule.hydra.oauth2.examples.model.User;

import sh.ory.hydra.api.AdminApi;
import sh.ory.hydra.model.AcceptConsentRequest;
import sh.ory.hydra.model.AcceptLoginRequest;
import sh.ory.hydra.model.CompletedRequest;
import sh.ory.hydra.model.ConsentRequest;
import sh.ory.hydra.model.ConsentRequestSession;
import sh.ory.hydra.model.LoginRequest;
import sh.ory.hydra.model.RejectRequest;

@Controller
@SuppressWarnings("unchecked")
public class HydraLoginController extends HttpApiResourceSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(HydraLoginController.class);
	
	@Autowired
	private AdminApi hydraAdminApi;
	
	/**
	 * 去登录页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@GetMapping(value="/login")
	public String loginRender(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String loginChallenge = request.getParameter("login_challenge");
		boolean hydraOAuth2Login = !StringUtils.isEmpty(loginChallenge);
		if(hydraOAuth2Login) { //如果是来自hydra的OAuth2(authorization_code)认证流程
			LOGGER.info("【getLoginRequest】>>> loginChallenge = {}", loginChallenge);
			//根据loginChallenge从ORY Hydra获取有关登录请求的信息
			LoginRequest loginResponse = hydraAdminApi.getLoginRequest(loginChallenge);
			LOGGER.info("【getLoginRequest】<<< loginResponse = {}", loginResponse);
			if(Boolean.TRUE.equals(loginResponse.getSkip())) { //如果hydra已经对用户进行过身份验证，则用户无需登录验证，即跳过登录步骤
				//此处可以做一些业务逻辑，例如更新用户登录次数
				AcceptLoginRequest acceptLoginRequest = new AcceptLoginRequest();
				acceptLoginRequest.setSubject(loginResponse.getSubject());
				LOGGER.info("【acceptLoginRequest】>>> acceptLoginRequest = {}", acceptLoginRequest);
				CompletedRequest acceptLoginResponse = hydraAdminApi.acceptLoginRequest(loginChallenge, acceptLoginRequest);
				LOGGER.info("【acceptLoginRequest】<<< acceptLoginResponse = {}", acceptLoginResponse);
				return "redirect:" + acceptLoginResponse.getRedirectTo(); //将用户重定向回hydra
			} else { //渲染login页面
				model.addAttribute("loginChallenge", loginChallenge);
			}
		}
		return "login";
	}
	
	/**
	 * 登录失败
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/login/failure")
	public String loginFailure(HttpServletRequest request, HttpServletResponse response, Model model) {
		String loginChallenge = request.getParameter("loginChallenge");
		if(!StringUtils.isEmpty(loginChallenge)) { //如果是hydra的OAuth2(authorization_code)认证流程
			model.addAttribute("loginChallenge", loginChallenge); //把loginChallenge参数重新带到页面上
		}
		Exception exception = SpringSecurityUtils.getAuthenticationException(request);
		LOGGER.error(String.format(">>> login failure: %s", exception.getMessage()), exception);
		model.addAttribute("error", Boolean.TRUE);
		model.addAttribute("message", ExceptionUtils.getRootCauseMessage(exception));
		return "login";
	}
	
	/**
	 * 登录成功
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/login/success")
	public String loginSuccess(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String loginChallenge = request.getParameter("loginChallenge");
		if(!StringUtils.isEmpty(loginChallenge)) { //如果是hydra的OAuth2(authorization_code)认证流程
			String username = request.getParameter("username");
			String remember = request.getParameter("remember");
			AcceptLoginRequest acceptLoginRequest = new AcceptLoginRequest();
			acceptLoginRequest.setSubject(username);
			acceptLoginRequest.setRemember("1".equals(remember) || "true".equals(remember));
			acceptLoginRequest.setRememberFor(getEnvironment().getProperty("server.servlet.session.timeout", Long.class, 7200L));
			LOGGER.info("【acceptLoginRequest】>>> acceptLoginRequest = {}", acceptLoginRequest);
			CompletedRequest acceptLoginResponse = hydraAdminApi.acceptLoginRequest(loginChallenge, acceptLoginRequest);
			LOGGER.info("【acceptLoginRequest】<<< acceptLoginResponse = {}", acceptLoginResponse);
			return "redirect:" + acceptLoginResponse.getRedirectTo(); //将用户重定向回hydra
		} else {
			return "redirect:/index";
		}
	}
	
	/**
	 * 同意页面渲染
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@GetMapping(value="/consent")
	public String consentRender(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String consentChallenge = request.getParameter("consent_challenge");
		boolean hydraOAuth2Login = !StringUtils.isEmpty(consentChallenge);
		if(hydraOAuth2Login) { //如果是来自hydra的OAuth2(authorization_code)认证流程
			//根据consentChallenge从ORY Hydra获取关于同意请求的信息
			LOGGER.info("【getConsentRequest】>>> consentChallenge = {}", consentChallenge);
			ConsentRequest consentResponse = hydraAdminApi.getConsentRequest(consentChallenge);
			LOGGER.info("【getConsentRequest】<<< consentResponse = {}", consentResponse);
			if(Boolean.TRUE.equals(consentResponse.getSkip())) { //如果用户已将请求的范围授予此应用程序
				//此处可以做一些业务逻辑，例如授予其他权限(scope)
				AcceptConsentRequest acceptConsentRequest = new AcceptConsentRequest();
				acceptConsentRequest.setGrantScope(consentResponse.getRequestedScope());
				acceptConsentRequest.setGrantAccessTokenAudience(consentResponse.getRequestedAccessTokenAudience());
				ConsentRequestSession session = new ConsentRequestSession(); //向access_token/id_token中添加的附属字段
				session.putAccessTokenItem("foo", "bar");
				session.putIdTokenItem("foo", "bar");
				acceptConsentRequest.setSession(session);
				LOGGER.info("【acceptConsentRequest】>>> acceptConsentRequest = {}", acceptConsentRequest);
				CompletedRequest acceptConsentResponse = hydraAdminApi.acceptConsentRequest(consentChallenge, acceptConsentRequest);
				LOGGER.info("【acceptConsentRequest】<<< acceptConsentResponse = {}", acceptConsentResponse);
				return "redirect:" + acceptConsentResponse.getRedirectTo(); //将用户重定向回hydra
			} else { //渲染consent页面
				model.addAttribute("consentChallenge", consentChallenge);
				model.addAttribute("oauth2Scopes", consentResponse.getRequestedScope());
				model.addAttribute("oauth2Subject", consentResponse.getSubject());
				model.addAttribute("oauth2Client", consentResponse.getClient());
			}
		}
		return "consent";
	}
	
	/**
	 * 同意操作
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@PostMapping(value="/consent")
	public String consentAccess(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String consentAccess = request.getParameter("consentAccess");
		if(consentAccess.equals("1")) {
			return consentAllowed(request, response, model);
		} else {
			return consentDenied(request, response, model);
		}
	}
	
	/**
	 * 同意授权
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public String consentAllowed(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String consentChallenge = request.getParameter("consentChallenge");
		String remember = request.getParameter("remember");
		Assert.hasText(consentChallenge, "The request parameter 'consentChallenge' not found!");
		String[] grantScopes = request.getParameterValues("grantScopes");
		//根据consentChallenge从ORY Hydra获取关于同意请求的信息
		LOGGER.info("【getConsentRequest】>>> consentChallenge = {}", consentChallenge);
		ConsentRequest consentResponse = hydraAdminApi.getConsentRequest(consentChallenge);
		LOGGER.info("【getConsentRequest】<<< consentResponse = {}", consentResponse);
		
		AcceptConsentRequest acceptConsentRequest = new AcceptConsentRequest();
		acceptConsentRequest.setGrantScope(Arrays.asList(grantScopes));
		acceptConsentRequest.setGrantAccessTokenAudience(consentResponse.getRequestedAccessTokenAudience());
		acceptConsentRequest.setRemember("1".equals(remember) || "true".equals(remember));
		acceptConsentRequest.setRememberFor(getEnvironment().getProperty("server.servlet.session.timeout", Long.class, 7200L));
		ConsentRequestSession session = new ConsentRequestSession(); //向access_token/id_token中添加的附属字段
		session.putAccessTokenItem("foo", "bar");
		session.putIdTokenItem("foo", "bar");
		acceptConsentRequest.setSession(session);
		LOGGER.info("【acceptConsentRequest】>>> acceptConsentRequest = {}", acceptConsentRequest);
		CompletedRequest acceptConsentResponse = hydraAdminApi.acceptConsentRequest(consentChallenge, acceptConsentRequest);
		LOGGER.info("【acceptConsentRequest】<<< acceptConsentResponse = {}", acceptConsentResponse);
		return "redirect:" + acceptConsentResponse.getRedirectTo(); //将用户重定向回hydra
	}
	
	/**
	 * 拒绝授权
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public String consentDenied(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String consentChallenge = request.getParameter("consentChallenge");
		Assert.hasText(consentChallenge, "The request parameter 'consentChallenge' not found!");
		String message = "The resource owner denied the request";
		LOGGER.error(">>> {}", message);
		RejectRequest rejectRequest = new RejectRequest();
		rejectRequest.setError("access_denied");
		rejectRequest.setErrorDescription(message);
		CompletedRequest rejectResponse = hydraAdminApi.rejectConsentRequest(consentChallenge, rejectRequest);
		return "redirect:" + rejectResponse.getRedirectTo();
	}
	
	/**
	 * 去首页
	 * @param authentication
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/index")
	public String index(AbstractAuthenticationToken authentication, Model model) {
		LOGGER.info(">>> authentication = {}", authentication);
		User user = (User) authentication.getPrincipal();
		Map<String,Object> userInfo = BeanMap.create(user);
		LOGGER.debug(">>> userInfo = {}", JsonUtils.object2Json(userInfo));
		model.addAttribute("userInfo", userInfo);
		return "index";
	}
	
	/**
	 * 退出成功
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/logout/success")
	public String logoutSuccess(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		return "redirect:/login";
	}
	
}
