package com.penglecode.xmodule.common.security.oauth2.client.util;

import org.springframework.util.Assert;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.token.BearerTokenError;

/**
 * 处理OAuth2 WWW_AUTHENTICATE请求头携带错误信息的工具类
 * 
 * @author 	pengpeng
 * @date 	2020年2月1日 下午1:24:56
 */
public class OAuth2ErrorUtils {

	/**
	 * 解析wwwAuthenticate请求头信息
	 * @param wwwAuthenticate
	 * @return
	 * @throws ParseException
	 */
	public static BearerTokenError parseBearerTokenError(String wwwAuthenticate) throws ParseException {
		Assert.hasText(wwwAuthenticate, "Parameter 'wwwAuthenticate' must be required!");
		return BearerTokenError.parse(wwwAuthenticate);
	}
	
	public static void main(String[] args) throws Exception {
		String wwwAuthenticate = "Bearer error=\"invalid_token\", error_description=\"An error occurred while attempting to decode the Jwt: Jwt expired at 2020-01-31T14:31:31Z\", error_uri=\"https://tools.ietf.org/html/rfc6750#section-3.1\"";
		BearerTokenError error = parseBearerTokenError(wwwAuthenticate);
		System.out.println(error);
	}
	
}
