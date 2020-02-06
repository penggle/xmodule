package com.penglecode.xmodule.common.security.oauth2.client.util;

import java.util.Map;

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
	
	/**
	 * 计算wwwAuthenticate头的值
	 * @param wwwAuthenticateParameters
	 * @return
	 */
	public static String computeWWWAuthenticateHeaderValue(Map<String, String> wwwAuthenticateParameters) {
		StringBuilder wwwAuthenticate = new StringBuilder();
		wwwAuthenticate.append("Bearer");

		if (!wwwAuthenticateParameters.isEmpty()) {
			wwwAuthenticate.append(" ");
			int i = 0;
			for (Map.Entry<String, String> entry : wwwAuthenticateParameters.entrySet()) {
				wwwAuthenticate.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
				if (i != wwwAuthenticateParameters.size() - 1) {
					wwwAuthenticate.append(", ");
				}
				i++;
			}
		}

		return wwwAuthenticate.toString();
	}
	
}
