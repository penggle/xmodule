package com.penglecode.xmodule.common.cloud.consts;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.penglecode.xmodule.common.consts.Constant;
import com.penglecode.xmodule.common.consts.SpringEnvConstant;
import com.penglecode.xmodule.common.support.NamedThreadFactory;

public class CloudApplicationConstants {

	public static final String HEADER_KEY_USE_NORMALIZED_RESPONSE_RESULT = "useNormalizedResponseResult";
	
	/** SpringCloud Bus的bus-id配置值 */
	public static final Constant<String> SPRING_CLOUD_BUS_ID = new SpringEnvConstant<String>("spring.cloud.bus.id") {};
	
	/**
	 * 事件发布默认的线程池
	 */
	public static final Executor DEFAULT_EVENT_PUBLISH_EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4, new NamedThreadFactory("DEFAULT-EVENT-PUBLISHER"));
	
}
