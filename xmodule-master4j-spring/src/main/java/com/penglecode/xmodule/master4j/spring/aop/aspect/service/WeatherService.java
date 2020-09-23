package com.penglecode.xmodule.master4j.spring.aop.aspect.service;

import com.penglecode.xmodule.master4j.spring.common.model.WeatherInfo;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/19 1:07
 */
public interface WeatherService extends RemoteService {

    public WeatherInfo getWeatherInfoByCity(String cityid);

}
