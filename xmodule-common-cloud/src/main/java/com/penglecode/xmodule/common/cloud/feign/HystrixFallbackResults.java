package com.penglecode.xmodule.common.cloud.feign;

import org.springframework.http.HttpStatus;

import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;

/**
 * 默认的熔断fallback结果
 * 
 * @author 	pengpeng
 * @date	2019年5月31日 下午3:48:40
 */
public class HystrixFallbackResults {

	public static final HttpStatus DEFAULT_FALLBACK_STATUS = HttpStatus.SERVICE_UNAVAILABLE;
	
	public static <T> Result<T> defaultFallbackResult() {
		return Result.failure().code(DEFAULT_FALLBACK_STATUS.value()).message(String.format("请求失败: %s", DEFAULT_FALLBACK_STATUS.getReasonPhrase())).build();
	}
	
	public static <T> PageResult<T> defaultFallbackPageResult() {
		return PageResult.failure().code(DEFAULT_FALLBACK_STATUS.value()).message(String.format("请求失败: %s", DEFAULT_FALLBACK_STATUS.getReasonPhrase())).build();
	}
	
}
