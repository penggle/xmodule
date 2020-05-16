package com.penglecode.xmodule.common.security.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.penglecode.xmodule.common.security.jwt.JwtToken;
import com.penglecode.xmodule.common.security.jwt.JwtTokenService;

/**
 * 基于标准令牌鉴权方式(Authorization: Bearer <token>)的JwtAuthenticationTokenFilter
 * 
 * 此过滤器主要是验证令牌的合法性，如果令牌合法，则获取用户信息，并且存入SecurityContextHolder
 * 
 * @author 	pengpeng
 * @date	2019年12月25日 下午12:53:24
 */
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

	/**
	 * 当前JWT令牌在请求Attribute中的Key
	 */
	public static final String JWT_TOKEN_REQUEST_ATTR_KEY = JwtTokenAuthenticationFilter.class.getName() + ".JwtToken";
	
	@Autowired
	private JwtTokenService<HttpServletRequest,HttpServletResponse> jwtTokenService;
	
	private final AntPathRequestMatcher loginRequestMatcher = new AntPathRequestMatcher("/api/login", "POST");
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(!loginRequestMatcher.matches(request)) {
			if(SecurityContextHolder.getContext().getAuthentication() == null) {
				JwtToken jwtToken = jwtTokenService.getValidToken(request); //从请求中解析有效的JWT令牌对象
				if(jwtToken != null) {
					UserDetails userDetails = jwtTokenService.loadUserByUsername(jwtToken.getUsername(), false); //优先从Redis缓存中加载登录用户信息,如果没有则走数据库
					// 将用户信息存入 authentication，方便后续校验
	                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                // 将 authentication 存入 ThreadLocal，方便后续获取用户信息
	                SecurityContextHolder.getContext().setAuthentication(authentication);
	                request.setAttribute(JWT_TOKEN_REQUEST_ATTR_KEY, jwtToken); //设置到请求属性中去
				}
			}
		}
		filterChain.doFilter(request, response);
	}
	
	protected JwtTokenService<HttpServletRequest,HttpServletResponse> getJwtTokenService() {
		return jwtTokenService;
	}

	protected AntPathRequestMatcher getLoginRequestMatcher() {
		return loginRequestMatcher;
	}

}
