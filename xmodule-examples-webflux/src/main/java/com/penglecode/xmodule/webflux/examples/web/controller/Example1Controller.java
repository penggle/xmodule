package com.penglecode.xmodule.webflux.examples.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.DateTimeUtils;

import reactor.core.publisher.Mono;

/**
 * 示例Controller
 * 
 * @author 	pengpeng
 * @date 	2020年1月16日 下午7:17:35
 */
@RestController
@RequestMapping("/api/example1")
public class Example1Controller {

	@GetMapping(value="/nowtime", produces=MediaType.APPLICATION_JSON_VALUE)
	public Mono<Object> getNowTime() {
		Result<Object> result = Result.success().data(DateTimeUtils.formatNow()).build();
		return Mono.just(result);
	}
	
	/**
	 * 如果value不是整数，则触发#NumberFormatException
	 * @param value
	 * @return
	 */
	@GetMapping(value="/try-exception1/{value}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Mono<Object> tryException1(@PathVariable("value") String value) {
		Result<Object> result = Result.success().data(Integer.valueOf(value)).build();
		return Mono.just(result);
	}
	
	/**
	 * 请使用GET请求请求该接口以触发：#HttpRequestMethodNotSupportedException
	 * @param value
	 * @return
	 */
	@PostMapping(value="/try-exception2/{value}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Mono<Object> tryException2(@PathVariable("value") String value) {
		Result<Object> result = Result.success().data(value).build();
		return Mono.just(result);
	}
	
	/**
	 * 测试通过设置Result.code来设置HTTP响应的状态码
	 * @return
	 */
	@GetMapping(value="/result", produces=MediaType.APPLICATION_JSON_VALUE)
	public Mono<Object> testResults() {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Result<Object> result = Result.failure().code(status.value()).message(status.getReasonPhrase()).build();
		return Mono.just(result);
	}
	
}
