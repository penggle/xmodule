package com.penglecode.xmodule.master4j.spring.aop.aspect.service;

import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.master4j.spring.common.model.WeatherInfo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * 国家气象局的天气预报接口
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/19 1:13
 */
@Service("weatherService")
public class WeatherServiceImpl implements WeatherService {

    private static final String API_URL = "http://www.weather.com.cn/data/cityinfo/{cityid}.html";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public WeatherInfo getWeatherInfoByCity(String cityid) {
        Assert.hasText(cityid, "Parameter 'cityid' can't be null!");
        String url = API_URL;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, String.class, Collections.singletonMap("cityid", cityid));
        if(response.getStatusCode().is2xxSuccessful()) {
            String json = response.getBody();
            try {
                json = new String(json.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return JsonUtils.json2Object(json, (objectMapper, rootNode) -> rootNode.get("weatherinfo").traverse(objectMapper).readValueAs(WeatherInfo.class));
        }
        throw new IllegalStateException(String.format("No weather data found! response is: code = %s, body = %s", response.getStatusCode(), response.getBody()));
    }
}
