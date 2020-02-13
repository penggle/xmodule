package com.penglecode.xmodule.common.cloud.feign.hystrix;

import java.lang.reflect.Method;

import org.springframework.cloud.openfeign.HystrixFallbackHandler;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.StringUtils;

import feign.FeignException;

/**
 * 默认的服务熔断|降级统一拦截处理类
 * 
 * @author 	pengpeng
 * @date	2019年6月3日 上午9:33:14
 */
public class DefaultHystrixFallbackHandler extends HystrixFallbackHandler {

	public DefaultHystrixFallbackHandler(Class<?> feignClientClass, Throwable cause) {
		super(feignClientClass, cause);
	}

	@Override
	protected Object handle(Object proxy, Method method, Object[] args) throws Throwable {
		Class<?> returnType = method.getReturnType();
		if(PageResult.class.equals(returnType)) {
			return handle4PageResult(method, args, getCause());
		} else if (Result.class.equals(returnType)) {
			return handle4Result(method, args, getCause());
		} else {
			throw getCause();
		}
	}
	
	protected PageResult<Object> handle4PageResult(Method method, Object[] args, Throwable cause) {
		if(cause instanceof FeignException) {
			FeignException feignException = (FeignException) cause;
			String responseBody = feignException.contentUTF8();
			if(JsonUtils.isJsonObject(responseBody)) {
				PageResult<Object> result = JsonUtils.json2Object(responseBody, new TypeReference<PageResult<Object>>() {});
				//如果接口响应是Result类型,那么直接返回该结果
				if(HttpStatus.resolve(result.getCode()) != null && !StringUtils.isEmpty(result.getMessage())) {
					return result;
				}
			}
		}
		return HystrixFallbackResults.defaultFallbackPageResult();
	}
	
	protected Result<Object> handle4Result(Method method, Object[] args, Throwable cause) {
		if(cause instanceof FeignException) {
			FeignException feignException = (FeignException) cause;
			String responseBody = feignException.contentUTF8();
			if(JsonUtils.isJsonObject(responseBody)) {
				Result<Object> result = JsonUtils.json2Object(responseBody, new TypeReference<Result<Object>>() {});
				//如果接口响应是Result类型,那么直接返回该结果
				if(HttpStatus.resolve(result.getCode()) != null && !StringUtils.isEmpty(result.getMessage())) {
					return result;
				}
			}
		}
		return HystrixFallbackResults.defaultFallbackResult();
	}
	
}
