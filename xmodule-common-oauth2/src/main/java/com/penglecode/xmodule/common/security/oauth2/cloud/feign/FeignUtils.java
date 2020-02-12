package com.penglecode.xmodule.common.security.oauth2.cloud.feign;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import feign.RequestTemplate;

/**
 * Feign相关工具类
 * 
 * @author 	pengpeng
 * @date	2019年6月25日 下午4:07:30
 */
public class FeignUtils {

	/**
	 * 清空header
	 * @param template
	 */
	public static void clearHeader(RequestTemplate template) {
		template.headers(null);
	}
	
	/**
	 * 设置header
	 * @param template
	 * @param name
	 * @param value
	 */
	public static void setHeader(RequestTemplate template, String name, String... values) {
		Map<String,Collection<String>> newHeaders = new HashMap<String,Collection<String>>();
		Map<String,Collection<String>> oldHeaders = template.headers();
		if(oldHeaders != null) {
			newHeaders.putAll(oldHeaders);
		}
		template.headers(null); //clear
		newHeaders.put(name, Arrays.asList(values));
		template.headers(newHeaders); //replaceAll
	}
	
}
