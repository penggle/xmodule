package com.penglecode.xmodule.examples.springcloud.sidecar.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

/**
 * spring-actuactor的服务监控检测指示器
 * 
 * @author 	pengpeng
 * @date	2018年9月6日 下午1:32:32
 */
@Component("applicationHealthIndicator")
public class ApplicationHealthIndicator implements ReactiveHealthIndicator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationHealthIndicator.class);
	
	@Override
	public Mono<Health> health() {
		Health health = new Health.Builder().up().build();
		LOGGER.info(">>> 检测应用健康状况：{}", health);
		return Mono.just(health);
	}
	
}
