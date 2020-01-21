package com.penglecode.xmodule.springboot.examples.web.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.DateTimeUtils;

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
	public Result<Object> getNowTime() {
		return Result.success().data(DateTimeUtils.formatNow()).build();
	}
	
	/**
	 * 如果value不是整数，则触发#NumberFormatException
	 * @param value
	 * @return
	 */
	@GetMapping(value="/try-exception1/{value}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> tryException1(@PathVariable("value") String value) {
		return Result.success().data(Integer.valueOf(value)).build();
	}
	
	/**
	 * 请使用GET请求请求该接口以触发：#HttpRequestMethodNotSupportedException
	 * @param value
	 * @return
	 */
	@PostMapping(value="/try-exception2/{value}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> tryException2(@PathVariable("value") String value) {
		return Result.success().data(value).build();
	}
	
}
