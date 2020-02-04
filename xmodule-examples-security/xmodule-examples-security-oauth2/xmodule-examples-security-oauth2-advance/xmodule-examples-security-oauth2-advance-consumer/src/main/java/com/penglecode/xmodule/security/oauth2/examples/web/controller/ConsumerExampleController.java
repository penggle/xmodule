package com.penglecode.xmodule.security.oauth2.examples.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.examples.springcloud.api.service.JokeApiService;
import com.penglecode.xmodule.examples.springcloud.api.service.NewsApiService;
import com.penglecode.xmodule.examples.springcloud.api.service.WeatherApiService;
import com.penglecode.xmodule.security.oauth2.examples.model.Joke;
import com.penglecode.xmodule.security.oauth2.examples.model.News;
import com.penglecode.xmodule.security.oauth2.examples.model.OpenApiResult;
import com.penglecode.xmodule.security.oauth2.examples.model.Weather;

@RestController
public class ConsumerExampleController {

	@Autowired
	private WeatherApiService weatherApiService;
	
	@Autowired
	private NewsApiService newsApiService;
	
	@Autowired
	private JokeApiService jokeApiService;
	
	@GetMapping(value="/api/weather/{province}/{city}/today", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Weather> getTodayWeather(@PathVariable("province") String province,@PathVariable("city") String city) {
		return weatherApiService.getTodayWeather(province, city);
	}
	
	@GetMapping(value="/api/news/list")
	public Result<List<News>> getNewsList(Integer page, Integer count) {
		OpenApiResult<List<News>> apiResult = newsApiService.getNewsList(page, count);
		return apiResult.toResult();
	}
	
	@GetMapping(value="/api/joke/list")
	public Result<List<Joke>> getJokeList(String type, Integer page, Integer count) {
		OpenApiResult<List<Joke>> apiResult = jokeApiService.getJokeList(type, page, count);
		return apiResult.toResult();
	}
	
	@GetMapping(value="/api/joke/{sid}")
	public Result<Joke> getJokeById(@PathVariable("sid") String sid) {
		OpenApiResult<Joke> apiResult = jokeApiService.getJokeById(sid);
		return apiResult.toResult();
	}
	
}
