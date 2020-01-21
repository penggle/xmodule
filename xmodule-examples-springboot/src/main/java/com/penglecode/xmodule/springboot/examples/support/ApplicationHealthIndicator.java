package com.penglecode.xmodule.springboot.examples.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.penglecode.xmodule.common.boot.actuator.ControllableHealthIndicator;

@Component("applicationHealthIndicator")
public class ApplicationHealthIndicator extends ControllableHealthIndicator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationHealthIndicator.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	protected void checkHealth(Builder builder) {
		builder.up();
		try {
			String healthSql = "select DATE_FORMAT(NOW(), '%Y-%m-%d %T')";
			jdbcTemplate.queryForObject(healthSql, String.class);
			builder.up();
		} catch (Throwable e) {
			builder.down().withException(e);
			LOGGER.error(String.format(">>> 检测应用健康状况发生错误：%s", e.getMessage()), e);
		}
		LOGGER.info(">>> 检测应用健康状况：{}", builder.build());
	}

}
