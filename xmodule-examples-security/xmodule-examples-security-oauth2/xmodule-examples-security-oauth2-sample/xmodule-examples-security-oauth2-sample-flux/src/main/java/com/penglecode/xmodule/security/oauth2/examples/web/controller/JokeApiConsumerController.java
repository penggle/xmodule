package com.penglecode.xmodule.security.oauth2.examples.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.penglecode.xmodule.common.security.oauth2.client.reactive.ServerOAuth2AuthorizedClientExchangeFilter;
import com.penglecode.xmodule.common.security.oauth2.consts.OAuth2ApplicationConstants;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.security.oauth2.examples.model.Joke;

import reactor.core.publisher.Mono;

/**
 * 基于OAuth2 client_credentials模式的应用API之间相互调用的鉴权
 * 
 * @author 	pengpeng
 * @date 	2020年2月3日 下午10:10:04
 */
@RestController
@RequestMapping("/api/joke")
public class JokeApiConsumerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(JokeApiConsumerController.class);
	
	@Resource(name="defaultApiWebClient")
	private WebClient apiWebClient;
	
	@GetMapping(value="/list", produces=MediaType.APPLICATION_JSON_VALUE)
	public Mono<Result<List<Joke>>> getJokeList(JwtAuthenticationToken authentication,
			@RequestParam(name="type") String type,
			@RequestParam(name="page", defaultValue="1") Integer page,
			@RequestParam(name="count", defaultValue="10") Integer count) {
		LOGGER.info(">>> jwt = {}", JsonUtils.object2Json(authentication.getToken()));
		LOGGER.info(">>> getJokeList({}, {}, {})", type, page, count);
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("type", type);
		parameter.put("page", page);
		parameter.put("count", count);
		Mono<Result<List<Joke>>> result = apiWebClient
				.get()
				.uri("http://127.0.0.1:8082/api/joke/list?type={type}&page={page}&count={count}", parameter)
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Result<List<Joke>>>() {});
		return result;
	}
	
	@GetMapping(value="/{sid}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Mono<Result<Joke>> getJokeBySid(JwtAuthenticationToken authentication, @PathVariable("sid") String sid) {
		LOGGER.info(">>> jwt = {}", JsonUtils.object2Json(authentication.getToken()));
		LOGGER.info(">>> getJokeBySid({})", sid);
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("sid", sid);
		Mono<Result<Joke>> result = apiWebClient
				.get()
				.uri("http://127.0.0.1:8082/api/joke/{sid}", parameter)
				//此接口调用指定使用password模式客户端(即当前登录用户的OAuth2AuthorizedClient)来进行WebClient请求OAuth2认证
				.attributes(ServerOAuth2AuthorizedClientExchangeFilter.withClientRegistration(OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getUserRegistrationId()))
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<Result<Joke>>() {});
		return result;
	}
	
}
