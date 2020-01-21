package com.penglecode.xmodule.common.util;

import org.springframework.web.reactive.function.server.ServerRequest;

/**
 * 基于Reactive的web应用工具类
 * 
 * @author 	pengpeng
 * @date 	2020年1月18日 上午10:43:13
 */
public class ReactiveWebUtils {

	/**
	 * 判断来的请求是否是ajax异步请求
	 * @param request
	 * @return
	 */
    public static boolean isAjaxRequest(ServerRequest request) {
    	return "XMLHttpRequest".equals(request.headers().asHttpHeaders().getFirst("X-Requested-With"));
    }
	
}
