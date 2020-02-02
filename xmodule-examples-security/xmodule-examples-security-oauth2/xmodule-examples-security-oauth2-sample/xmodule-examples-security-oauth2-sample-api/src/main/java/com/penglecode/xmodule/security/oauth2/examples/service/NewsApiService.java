package com.penglecode.xmodule.security.oauth2.examples.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.security.oauth2.examples.model.News;
import com.penglecode.xmodule.security.oauth2.examples.model.OpenApiResult;

@Service("newsApiService")
public class NewsApiService {

	@Resource(name="defaultRestTemplate")
	private RestTemplate defaultRestTemplate;
	
	public OpenApiResult<List<News>> getNewsList(Integer page, Integer count) {
		String url = "https://api.apiopen.top/getWangYiNews?page={page}&count={count}";
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("page", ObjectUtils.defaultIfNull(page, 1));
		parameter.put("count", ObjectUtils.defaultIfNull(count, 10));
		ResponseEntity<String> response = defaultRestTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, String.class, parameter);
		HttpStatus status = response.getStatusCode();
		String body = response.getBody();
		if(status.is2xxSuccessful()) {
			return JsonUtils.json2Object(body, new TypeReference<OpenApiResult<List<News>>>() {});
		} else {
			return new OpenApiResult<List<News>>(status.value(), String.format("%s: %s", status.name(), body), null);
		}
	}

}
