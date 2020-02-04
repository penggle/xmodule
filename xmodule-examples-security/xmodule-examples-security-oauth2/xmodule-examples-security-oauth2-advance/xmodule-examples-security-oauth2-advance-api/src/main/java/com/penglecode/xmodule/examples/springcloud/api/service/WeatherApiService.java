package com.penglecode.xmodule.examples.springcloud.api.service;

import org.springframework.cloud.openfeign.DefaultHystrixFallbackFactory;
import org.springframework.cloud.openfeign.FallbackableFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.security.oauth2.examples.model.Weather;

/**
 * producer提供的API
 * 
 * @author 	pengpeng
 * @date 	2020年1月18日 下午5:31:49
 */
@FeignClient(name="xmodule-examples-security-oauth2-advance-producer", qualifier="weatherApiService", contextId="weatherApiService", fallbackFactory=DefaultHystrixFallbackFactory.class)
public interface WeatherApiService extends FallbackableFeignClient {

	@GetMapping(value="/api/weather/{province}/{city}/today", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Weather> getTodayWeather(@PathVariable("province") String province,@PathVariable("city") String city);
	
}
