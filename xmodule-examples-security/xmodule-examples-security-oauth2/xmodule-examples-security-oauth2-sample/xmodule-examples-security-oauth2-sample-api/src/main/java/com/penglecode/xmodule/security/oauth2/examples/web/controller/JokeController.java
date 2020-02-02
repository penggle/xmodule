package com.penglecode.xmodule.security.oauth2.examples.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.web.servlet.support.HttpApiResourceSupport;
import com.penglecode.xmodule.security.oauth2.examples.model.Joke;
import com.penglecode.xmodule.security.oauth2.examples.service.JokeApiService;

@RestController
@RequestMapping("/api/joke")
public class JokeController extends HttpApiResourceSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(JokeController.class);
	
	@Autowired
	private JokeApiService jokeApiService;
	
	@GetMapping(value="/list", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<List<Joke>> getJokeList(
			@AuthenticationPrincipal Jwt jwt,
			@RequestParam(name="type") String type,
			@RequestParam(name="page", defaultValue="1") Integer page,
			@RequestParam(name="count", defaultValue="10") Integer count) {
		LOGGER.info(">>> jwt = {}", JsonUtils.object2Json(jwt));
		LOGGER.info(">>> getJokeList({}, {}, {})", type, page, count);
		return jokeApiService.getJokeList(type, page, count).toResult();
	}
	
	@GetMapping(value="/{sid}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Joke> getJokeBySid(@AuthenticationPrincipal Jwt jwt, @PathVariable("sid") String sid) {
		LOGGER.info(">>> jwt = {}", JsonUtils.object2Json(jwt));
		LOGGER.info(">>> getJokeBySid({})", sid);
		return jokeApiService.getJokeById(sid).toResult();
	}
	
}
