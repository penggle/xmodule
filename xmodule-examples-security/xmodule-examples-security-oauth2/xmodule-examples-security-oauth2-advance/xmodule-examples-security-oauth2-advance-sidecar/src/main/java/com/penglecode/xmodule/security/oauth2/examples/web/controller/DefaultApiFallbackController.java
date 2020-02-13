package com.penglecode.xmodule.security.oauth2.examples.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.cloud.feign.hystrix.HystrixFallbackResults;

/**
 * 默认的API熔断Fallback Controller
 * 
 * @author 	pengpeng
 * @date	2019年7月2日 下午5:08:13
 */
@RestController
public class DefaultApiFallbackController {

	@RequestMapping("/api/fallback")
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public Object apiFallback() {
		return HystrixFallbackResults.defaultFallbackResult();
	}
	
}
