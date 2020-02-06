package com.penglecode.xmodule.common.util;

import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.function.server.EntityResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring WebFlux工具类
 * 
 * @author 	pengpeng
 * @date 	2020年1月18日 上午10:38:31
 */
@SuppressWarnings("unchecked")
public class SpringWebFluxUtils {

	/**
	 * 获取当前ServerWebExchange
	 * @return
	 */
	public static Mono<ServerWebExchange> getCurrentServerWebExchange() {
		return Mono.subscriberContext()
				    .filter(c -> c.hasKey(ServerWebExchange.class))
				    .map(c -> c.get(ServerWebExchange.class));
	}
	
	/**
	 * 通过SpringWebFlux的HttpMessageWriter来输出响应
	 * 
	 * @param exchange
	 * @param responseBody				- 可以是普通对象,Mono,Flux
	 * @param responseMediaType			- 响应类型
	 * @return
	 */
	public static Mono<Void> writeHttpMessage(ServerWebExchange exchange, Object responseBody, MediaType responseMediaType) {
		ServerResponse.Context context = new CustomServerResponseContext(SpringUtils.getBean(ServerCodecConfigurer.class));
		final MediaType contentType = responseMediaType = ObjectUtils.defaultIfNull(responseMediaType, MediaType.APPLICATION_JSON);
		if(responseBody instanceof Mono) {
			return ((Mono<Object>) responseBody).map(body -> {
				if(!(body instanceof EntityResponse)) {
					return EntityResponse.fromObject(body).status(exchange.getResponse().getStatusCode()).contentType(contentType).build();
				}
				return body;
			}).cast(EntityResponse.class).flatMap(entityResponse -> entityResponse.writeTo(exchange, context));
		} else if (responseBody instanceof Flux) {
			return ((Flux<Object>) responseBody).collectList().map(objects -> {
				return EntityResponse.fromObject(objects).status(exchange.getResponse().getStatusCode()).contentType(contentType).build();
			}).cast(EntityResponse.class).flatMap(entityResponse -> entityResponse.writeTo(exchange, context));
		} else {
			Mono<EntityResponse<Object>> monoEntityResponse = EntityResponse.fromObject(responseBody).status(exchange.getResponse().getStatusCode()).contentType(contentType).build();
			return monoEntityResponse.flatMap(entityResponse -> entityResponse.writeTo(exchange, context));
		}
	}
	
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
	
	public static class CustomServerResponseContext implements ServerResponse.Context {

		private final ServerCodecConfigurer serverCodecConfigurer;
		
		public CustomServerResponseContext(ServerCodecConfigurer serverCodecConfigurer) {
			super();
			Assert.notNull(serverCodecConfigurer, "Parameter 'serverCodecConfigurer'");
			this.serverCodecConfigurer = serverCodecConfigurer;
		}

		@Override
		public List<HttpMessageWriter<?>> messageWriters() {
			return serverCodecConfigurer.getWriters();
		}

		@Override
		public List<ViewResolver> viewResolvers() {
			return Collections.EMPTY_LIST;
		}
		
	}
	
}
