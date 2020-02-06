package com.penglecode.xmodule.common.security.servlet.filter;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import com.penglecode.xmodule.common.security.config.JwtTokenConfigProperties;
import com.penglecode.xmodule.common.security.jwt.JwtToken;
import com.penglecode.xmodule.common.security.jwt.JwtTokenService;
import com.penglecode.xmodule.common.security.jwt.TokenSource;
import com.penglecode.xmodule.common.security.servlet.util.SpringSecurityUtils;

/**
 * JWT令牌自动续约过滤器
 * 
 * 前提是客户端(浏览器)是基于Cookie的方式来存储JWT令牌(而不是localStorage)
 * 
 * @author 	pengpeng
 * @date	2019年12月25日 下午12:53:24
 */
public class JwtTokenAutoRenewalFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenAutoRenewalFilter.class);
	
	@Autowired
	private JwtTokenConfigProperties jwtTokenConfig;
	
	@Autowired
	private JwtTokenService<HttpServletRequest,HttpServletResponse> jwtTokenService;
	
	private final AntPathRequestMatcher loginRequestMatcher = new AntPathRequestMatcher("/api/logout");
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(!loginRequestMatcher.matches(request)) {
			JwtToken jwtToken = getCurrentJwtToken(request);
			if(jwtToken != null && jwtToken.isValid() && TokenSource.COOKIE.equals(jwtToken.getSource())) { //如果Cookie作为JWT令牌的载体，并且当前JWT令牌是有效的，则提前校验令牌的有效期，并根据情况自动续约
				UserDetails loginUser = SpringSecurityUtils.getCurrentAuthenticatedUser();
				Instant expiresAt = jwtToken.getExpiresAt();
				boolean needRenewal = !expiresAt.minusSeconds(jwtTokenConfig.getRenewalAheadTime()).isAfter(Instant.now()); //是否需要提前刷新JWT令牌?
				if(needRenewal) { //需要提前续约?
					LOGGER.info(">>> Automatically renewal the JWT token in advance by cookie：{}", jwtToken);
					Assert.notNull(loginUser, "No UserDetails found in current spring security context!");
					jwtTokenService.issueToken(request, response, loginUser);
				}
			}
		}
		filterChain.doFilter(request, response);
	}
	
	/**
	 * 获取当前绑定在请求上下文中的JWT令牌对象
	 * @return
	 */
	protected JwtToken getCurrentJwtToken(HttpServletRequest request) {
		return (JwtToken) request.getAttribute(JwtTokenAuthenticationFilter.JWT_TOKEN_REQUEST_ATTR_KEY);
	}
	
	protected JwtTokenService<HttpServletRequest,HttpServletResponse> getJwtTokenService() {
		return jwtTokenService;
	}

}
