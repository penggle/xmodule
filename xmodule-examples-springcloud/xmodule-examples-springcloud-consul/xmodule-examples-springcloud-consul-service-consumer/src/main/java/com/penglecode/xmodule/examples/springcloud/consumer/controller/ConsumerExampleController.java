package com.penglecode.xmodule.examples.springcloud.consumer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.examples.springcloud.api.model.Joke;
import com.penglecode.xmodule.examples.springcloud.api.model.News;
import com.penglecode.xmodule.examples.springcloud.api.model.OpenApiResult;
import com.penglecode.xmodule.examples.springcloud.api.model.User;
import com.penglecode.xmodule.examples.springcloud.api.service.JokeApiService;
import com.penglecode.xmodule.examples.springcloud.api.service.NewsApiService;
import com.penglecode.xmodule.examples.springcloud.api.service.UserApiService;

@RestController
public class ConsumerExampleController {

	@Autowired
	private UserApiService userApiService;
	
	@Autowired
	private NewsApiService newsApiService;
	
	@Autowired
	private JokeApiService jokeApiService;
	
	@PostMapping(value="/api/user/register", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Result<Long> registerUser(@RequestBody User user) {
		return userApiService.registerUser(user);
	}
	
	@GetMapping(value="/api/user/{userId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<User> getUserById(@PathVariable("userId") Long userId) {
		return userApiService.getUserById(userId);
	}
	
	@GetMapping(value="/api/user/list", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<List<User>> getUserList(@RequestParam User condition) {
		return userApiService.getUserList(condition);
	}

	@GetMapping(value="/api/news/list")
	public Result<List<News>> getNewsList(Integer page, Integer count) {
		OpenApiResult<List<News>> apiResult = newsApiService.getNewsList(page, count);
		if(apiResult.getCode() == 200) {
			return Result.success().message(apiResult.getMessage()).data(apiResult.getResult()).build();
		} else {
			return Result.failure().message(apiResult.getMessage()).build();
		}
	}
	
	@GetMapping(value="/api/joke/list")
	public Result<List<Joke>> getJokeList(String type, Integer page, Integer count) {
		OpenApiResult<List<Joke>> apiResult = jokeApiService.getJokeList(type, page, count);
		if(apiResult.getCode() == 200) {
			return Result.success().message(apiResult.getMessage()).data(apiResult.getResult()).build();
		} else {
			return Result.failure().message(apiResult.getMessage()).build();
		}
	}
	
	@GetMapping(value="/api/joke/{sid}")
	public Result<Joke> getJokeById(@PathVariable("sid") String sid) {
		OpenApiResult<Joke> apiResult = jokeApiService.getJokeById(sid);
		if(apiResult.getCode() == 200) {
			return Result.success().message(apiResult.getMessage()).data(apiResult.getResult()).build();
		} else {
			return Result.failure().message(apiResult.getMessage()).build();
		}
	}
	
}
