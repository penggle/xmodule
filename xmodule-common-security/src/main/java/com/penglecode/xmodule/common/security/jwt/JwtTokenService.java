package com.penglecode.xmodule.common.security.jwt;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * JWT令牌服务
 * @param <I>	- 请求
 * @param <O>  - 响应
 * @author 	pengpeng
 * @date 	2020年1月17日 下午2:32:47
 */
public interface JwtTokenService<I,O> {

	/**
	 * 下发的JWT令牌的Redis缓存Key前缀
	 */
	public static final String JWT_TOKEN_KEY_PREFIX = "JWT_TOKEN_";
	
	/**
	 * 在下发新JWT令牌之后旧的JWT令牌的Redis缓存Key前缀
	 * 即下发新JWT令牌之后旧的JWT令牌将还能短暂的使用一会以解决前端在刷新令牌的时候出现新旧JWT令牌几乎同时到达服务端引起的问题
	 */
	public static final String JWT_TOKEN_OLD_KEY_PREFIX = "JWT_TOKEN_OLD_";
	
	/**
	 * 已登录用户的Redis缓存Key前缀
	 */
	public static final String LOGIN_USER_KEY_PREFIX = "LOGIN_USER_";
	
	/**
	 * 从请求中解析出有效的JWT令牌
	 * 1、JWT令牌本身签名是有效的(没有被篡改)
	 * 2、JWT令牌来源是一致的
	 * 3、JWT令牌本身没有过期
	 * 4、JWT令牌在业务上是有效的(比如在Redis中存储了一份与其进行比对)
	 * @param request
	 * @return
	 */
	public JwtToken getValidToken(I request);
	
	/**
	 * 将JWT令牌值转为JwtToken对象
	 * @param tokenValue
	 * @return
	 */
	public JwtToken fromTokenValue(String tokenValue);
	
	/**
	 * 下发JWT令牌
	 * @param request
	 * @param response
	 * @param loginUser
	 * @return
	 */
	public JwtAccessToken issueToken(I request, O response, UserDetails loginUser);
	
	/**
	 * 撤回指定登录用户的所有Jwt令牌(强制用户下线)
	 * @param username
	 */
	public void revokeToken(String username);
	
	/**
	 * 根据用户名批量获取用户的当前Jwt令牌
	 * @param usernames
	 * @return
	 */
	public Map<String,String> getTokenByUsernames(List<String> usernames);
	
	/**
	 * 根据用户名获取登录用户信息
	 * 该接口用于替代#UserDetailsService.loadUserByUsername()
	 * @param username
	 * @return
	 */
	public UserDetails loadUserByUsername(String username);
	
}
