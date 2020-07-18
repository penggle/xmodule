package com.penglecode.xmodule.common.cloud.consts;

import com.penglecode.xmodule.common.consts.Constant;
import com.penglecode.xmodule.common.consts.SpringEnvConstant;
import com.penglecode.xmodule.common.support.NamedThreadFactory;
import com.penglecode.xmodule.common.util.ThreadPoolUtils;

import java.util.concurrent.Executor;

public class CloudApplicationConstants {

	public static final String HEADER_KEY_USE_NORMALIZED_RESPONSE_RESULT = "useNormalizedResponseResult";
	
	/** SpringCloud Bus的bus-id配置值 */
	public static final Constant<String> SPRING_CLOUD_BUS_ID = new SpringEnvConstant<String>("spring.cloud.bus.id") {};
	
	/**
	 * 事件发布默认的线程池
	 */
	public static final Executor DEFAULT_EVENT_PUBLISH_EXECUTOR = ThreadPoolUtils.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4, new NamedThreadFactory("DEFAULT-EVENT-PUBLISHER"));
	
}
