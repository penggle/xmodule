package com.penglecode.xmodule.security.oauth2.examples.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.penglecode.xmodule.common.security.oauth2.client.servlet.util.OAuth2ClientUtils;
import com.penglecode.xmodule.common.security.servlet.util.SpringSecurityUtils;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.JsonUtils.JacksonJsonException;
import com.penglecode.xmodule.examples.springcloud.api.service.WeatherApiService;
import com.penglecode.xmodule.security.oauth2.examples.model.Weather;

@RestController("weatherController")
public class WeatherController implements WeatherApiService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WeatherController.class);
	
	@Resource(name="defaultRestTemplate")
	private RestTemplate restTemplate;
	
	@Override
	public Result<Weather> getTodayWeather(String province, String city) {
		JwtAuthenticationToken authentication = SpringSecurityUtils.getCurrentAuthentication();
		OAuth2AuthorizedClient authorizedClient = OAuth2ClientUtils.getAgreedOAuth2AuthorizedClient();
		LOGGER.info("【getTodayWeather】>>> authentication = {}, authorizedClient = {}", authentication, authorizedClient);
		
		String weatherType = "observe";
		String url = "https://wis.qq.com/weather/common?source=xw&weather_type={weatherType}&province={province}&city={city}";
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("weatherType", weatherType);
		parameter.put("province", province);
		parameter.put("city", city);
		long start = System.currentTimeMillis();
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, String.class, parameter);
		long end = System.currentTimeMillis();
		System.out.println(">>> cost " + (end - start) + " ms");
		HttpStatus status = response.getStatusCode();
		String body = response.getBody();
		if(status.is2xxSuccessful()) {
			ObjectMapper objectMapper = JsonUtils.createDefaultObjectMapper();
			JsonNode jsonNode = JsonUtils.createRootJsonNode(objectMapper, body);
			try {
				Weather weather = jsonNode.findValue(weatherType).traverse(objectMapper).readValueAs(Weather.class);
				return Result.success().data(weather).build();
			} catch (IOException e) {
				throw new JacksonJsonException(e.getMessage(), e);
			}
		} else {
			return Result.failure().code(status.value()).message(String.format("%s: %s", status.name(), body)).build();
		}
	}
	
}
