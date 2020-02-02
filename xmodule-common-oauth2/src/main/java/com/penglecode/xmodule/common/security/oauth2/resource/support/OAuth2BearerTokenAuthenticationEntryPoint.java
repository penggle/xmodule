package com.penglecode.xmodule.common.security.oauth2.resource.support;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.token.BearerTokenError;
import com.penglecode.xmodule.common.security.oauth2.client.util.OAuth2ErrorUtils;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.SpringWebMvcUtils;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * 自定义的OAuth2资源服务器AuthenticationEntryPoint
 * 
 * @author 	pengpeng
 * @date 	2020年2月1日 下午1:42:50
 */
public class OAuth2BearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private AuthenticationEntryPoint delegateAuthenticationEntryPoint = new BearerTokenAuthenticationEntryPoint();
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		delegateAuthenticationEntryPoint.commence(request, response, authException);
		postHandle(request, response, authException);
	}
	
	protected void postHandle(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		String wwwAuthenticate = response.getHeader(HttpHeaders.WWW_AUTHENTICATE);
		HttpStatus status = HttpStatus.resolve(response.getStatus());
		int code = status.value();
		String message = status.getReasonPhrase();
		if(!StringUtils.isEmpty(wwwAuthenticate)) {
			try {
				BearerTokenError error = OAuth2ErrorUtils.parseBearerTokenError(wwwAuthenticate);
				if(!StringUtils.isEmpty(error.getCode()) && !StringUtils.isEmpty(error.getDescription())) {
					message = String.format("%s: %s", error.getCode(), error.getDescription());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Result<Object> result = Result.failure().code(code).message(message).build();
		SpringWebMvcUtils.writeHttpMessage(request, response, result, MediaType.APPLICATION_JSON);
	}

	protected AuthenticationEntryPoint getDelegateAuthenticationEntryPoint() {
		return delegateAuthenticationEntryPoint;
	}

	public void setDelegateAuthenticationEntryPoint(AuthenticationEntryPoint delegateAuthenticationEntryPoint) {
		this.delegateAuthenticationEntryPoint = delegateAuthenticationEntryPoint;
	}

}
