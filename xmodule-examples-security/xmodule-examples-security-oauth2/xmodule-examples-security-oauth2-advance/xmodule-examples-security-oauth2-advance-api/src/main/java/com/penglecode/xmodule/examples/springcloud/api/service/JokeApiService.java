package com.penglecode.xmodule.examples.springcloud.api.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.penglecode.xmodule.security.oauth2.examples.model.Joke;
import com.penglecode.xmodule.security.oauth2.examples.model.OpenApiResult;


/**
 * 基于https://api.apiopen.top/api.html上的开放API接口
 * 
 * 此处通过@FeignClient(name="openapi")使得JokeApiService直连api.apiopen.top
 * 
 * @author 	pengpeng
 * @date 	2020年1月18日 下午5:18:17
 */
@FeignClient(name="openapi", qualifier="jokeApiService", contextId="jokeApiService")
public interface JokeApiService {

	@GetMapping(value="/getSingleJoke", produces=MediaType.APPLICATION_JSON_VALUE)
	public OpenApiResult<Joke> getJokeById(@RequestParam(name="sid") String sid);
	
	@GetMapping(value="/getJoke")
	public OpenApiResult<List<Joke>> getJokeList(@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "count", defaultValue = "10") Integer count);
	
}
