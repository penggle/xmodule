package com.penglecode.xmodule.master4j.springboot.example.common.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.master4j.springboot.example.common.model.Joke;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Joke服务
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/3 13:22
 */
@Service
public class JokeApiService {

    private static final String API_URL_JOKE_LIST = "https://api.apiopen.top/getJoke?type={type}&page={page}&count={count}";

    private static final String API_URL_JOKE_DETAIL = "https://api.apiopen.top/getSingleJoke?sid={sid}";

    @Resource(name="defaultRestTemplate")
    private RestTemplate restTemplate;

    public List<Joke> getJokeList(String type, Integer page, Integer count) {
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("type", StringUtils.defaultIfEmpty(type, ""));
        parameter.put("page", Math.max(ObjectUtils.defaultIfNull(page, 1), 1));
        parameter.put("count", Math.max(ObjectUtils.defaultIfNull(page, 10), 10));

        ResponseEntity<String> response = restTemplate.exchange(API_URL_JOKE_LIST, HttpMethod.GET, HttpEntity.EMPTY, String.class, parameter);
        if(response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            return JsonUtils.json2Object(responseBody, (objectMapper, rootNode) -> {
                JsonNode jsonNode = rootNode.get("result");
                if(jsonNode != null && !jsonNode.isNull() && jsonNode.isArray()) {
                    return jsonNode.traverse(objectMapper).readValueAs(new TypeReference<List<Joke>>() {});
                }
                return Collections.emptyList();
            });
        }
        return Collections.emptyList();
    }

    public Joke getJokeBySid(String sid) {
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("sid", sid);

        ResponseEntity<String> response = restTemplate.exchange(API_URL_JOKE_DETAIL, HttpMethod.GET, HttpEntity.EMPTY, String.class, parameter);
        if(response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            return JsonUtils.json2Object(responseBody, (objectMapper, rootNode) -> {
                JsonNode jsonNode = rootNode.get("result");
                if(jsonNode != null && !jsonNode.isNull()) {
                    return jsonNode.traverse(objectMapper).readValueAs(Joke.class);
                }
                return null;
            });
        }
        return null;
    }

}
