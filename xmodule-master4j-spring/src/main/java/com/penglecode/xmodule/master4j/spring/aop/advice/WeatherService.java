package com.penglecode.xmodule.master4j.spring.aop.advice;

import com.penglecode.xmodule.master4j.spring.common.model.WeatherInfo;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/19 1:07
 */
public interface WeatherService {

    public WeatherInfo getWeatherInfoByCity(String cityid, String token);

}
