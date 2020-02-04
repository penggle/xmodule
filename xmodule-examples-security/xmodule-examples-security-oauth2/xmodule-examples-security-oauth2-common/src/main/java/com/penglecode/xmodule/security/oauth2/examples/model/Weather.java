package com.penglecode.xmodule.security.oauth2.examples.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.penglecode.xmodule.common.support.BaseModel;

/**
 * 天气 - 数据模型
 * 
 * @author 	pengpeng
 * @date 	2020年2月4日 上午10:40:07
 */
public class Weather implements BaseModel<Weather> {

	private static final long serialVersionUID = 1L;

	private String weather;
	
	@JsonProperty("weather_code")
	private String weatherCode;
	
	@JsonProperty("weather_short")
	private String weatherShort;
	
	@JsonProperty("wind_direction")
	private String windDirection;
	
	@JsonProperty("wind_power")
	private String windPower;
	
	private String degree;
	
	private String humidity;
	
	private String precipitation;
	
	private String pressure;
	
	@JsonProperty("update_time")
	private String updateTime;

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getWeatherCode() {
		return weatherCode;
	}

	public void setWeatherCode(String weatherCode) {
		this.weatherCode = weatherCode;
	}

	public String getWeatherShort() {
		return weatherShort;
	}

	public void setWeatherShort(String weatherShort) {
		this.weatherShort = weatherShort;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public String getWindPower() {
		return windPower;
	}

	public void setWindPower(String windPower) {
		this.windPower = windPower;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(String precipitation) {
		this.precipitation = precipitation;
	}

	public String getPressure() {
		return pressure;
	}

	public void setPressure(String pressure) {
		this.pressure = pressure;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
}
