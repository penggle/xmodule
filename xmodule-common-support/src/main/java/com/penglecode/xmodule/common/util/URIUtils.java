package com.penglecode.xmodule.common.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.exception.ApplicationRuntimeException;

/**
 * URI/URL工具类
 * 
 * @author 	pengpeng
 * @date	2018年11月16日 上午9:52:48
 */
public class URIUtils {

	public static final Map<String,Integer> HTTP_PROTOCOLS = new HashMap<String,Integer>();
	
	static {
		HTTP_PROTOCOLS.put("http", 80);
		HTTP_PROTOCOLS.put("https", 443);
	}
	
	/**
	 * 判断字符串是否是URL
	 * @param url
	 * @return
	 */
	public static boolean isURL(String url) {
		try {
			new URL(url);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}
	
	/**
	 * 判断字符串是否是HTTP URL
	 * @param url
	 * @return
	 */
	public static boolean isHttpURL(String url) {
		try {
			URL urlObj = new URL(url);
			return HTTP_PROTOCOLS.containsKey(urlObj.getProtocol().toLowerCase());
		} catch (MalformedURLException e) {
			return false;
		}
	}
	
	/**
	 * 创建URL
	 * @param url
	 * @return
	 */
	public static URL createURL(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new ApplicationRuntimeException(e);
		}
	}
	
	/**
	 * 判断字符串是否是URI
	 * @param url
	 * @return
	 */
	public static boolean isURI(String uri) {
		try {
			new URI(uri);
			return true;
		} catch (URISyntaxException e) {
			return false;
		}
	}
	
	/**
	 * 创建URI
	 * @param uri
	 * @return
	 * @throws URISyntaxException 
	 */
	public static URI createURI(String uri) {
		try {
			return new URI(uri);
		} catch (URISyntaxException e) {
			throw new ApplicationRuntimeException(e);
		}
	}
	
	/**
	 * 创建URI
	 * @param url
	 * @param queryParams
	 * @return
	 */
	public static URI createURI(String url, Map<String,Object> queryParams) {
		return UriComponentsBuilder.fromHttpUrl(url).build(queryParams);
	}
	
	/**
	 * 获取端口号
	 * @param url
	 * @return
	 */
	public static int getPort(URL url) {
		int port = url.getPort();
		return port == -1 ? HTTP_PROTOCOLS.get(url.getProtocol()) : port;
	}
	
	/**
	 * 提取指定url后面的query参数
	 * @param url
	 * @return
	 */
	public static MultiValueMap<String,String> getQueryParams(String url) {
		MultiValueMap<String,String> paramMap = UriComponentsBuilder.fromHttpUrl(url).build().getQueryParams();
		MultiValueMap<String,String> queryParams = new LinkedMultiValueMap<>();
		if(paramMap != null) {
			queryParams.putAll(paramMap);
		}
		for(Map.Entry<String,List<String>> entry : queryParams.entrySet()) {
			List<String> values = entry.getValue();
			if(!CollectionUtils.isEmpty(values)) {
				values = values.stream().map(v -> {
					try {
						return URLDecoder.decode(v, GlobalConstants.DEFAULT_CHARSET);
					} catch (UnsupportedEncodingException e) {
						return v;
					}
				}).collect(Collectors.toList());
			}
			entry.setValue(values);
		}
		return queryParams;
	}
	
}
