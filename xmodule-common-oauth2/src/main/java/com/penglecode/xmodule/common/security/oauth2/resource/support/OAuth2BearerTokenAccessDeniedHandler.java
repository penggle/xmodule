package com.penglecode.xmodule.common.security.oauth2.resource.support;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.token.BearerTokenError;
import com.penglecode.xmodule.common.security.oauth2.client.util.OAuth2ErrorUtils;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.SpringWebMvcUtils;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * 自定义的OAuth2资源服务器AccessDeniedHandler
 * 
 * @author 	pengpeng
 * @date 	2020年2月1日 下午2:06:14
 */
public class OAuth2BearerTokenAccessDeniedHandler implements AccessDeniedHandler {

	private AccessDeniedHandler delegateAccessDeniedHandler = new BearerTokenAccessDeniedHandler();
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		delegateAccessDeniedHandler.handle(request, response, accessDeniedException);
		postHandle(request, response, accessDeniedException);
	}
	
	protected void postHandle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
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

	protected AccessDeniedHandler getDelegateAccessDeniedHandler() {
		return delegateAccessDeniedHandler;
	}

	public void setDelegateAccessDeniedHandler(AccessDeniedHandler delegateAccessDeniedHandler) {
		this.delegateAccessDeniedHandler = delegateAccessDeniedHandler;
	}
	
}
