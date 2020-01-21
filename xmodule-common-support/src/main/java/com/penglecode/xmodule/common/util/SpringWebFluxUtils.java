package com.penglecode.xmodule.common.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.function.server.ServerRequest;

/**
 * Spring WebFlux工具类
 * 
 * @author 	pengpeng
 * @date 	2020年1月18日 上午10:38:31
 */
public class SpringWebFluxUtils {

	/**
	 * 判断请求是否是异步请求
	 * @param request
	 * @param handler
	 * @return
	 */
	public static boolean isAsyncRequest(ServerRequest request){
		boolean isAsync = false;
		Object handler = request.exchange().getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
		if(handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if(handlerMethod != null){
				isAsync = handlerMethod.hasMethodAnnotation(ResponseBody.class);
				if(!isAsync){
					Class<?> controllerClass = handlerMethod.getBeanType();
					isAsync = controllerClass.isAnnotationPresent(ResponseBody.class) || controllerClass.isAnnotationPresent(RestController.class);
				}
				if(!isAsync){
					isAsync = ResponseEntity.class.equals(handlerMethod.getMethod().getReturnType());
				}
			}
			if(!isAsync){
				isAsync = ReactiveWebUtils.isAjaxRequest(request);
			}
		}
		return isAsync;
	}
	
}
