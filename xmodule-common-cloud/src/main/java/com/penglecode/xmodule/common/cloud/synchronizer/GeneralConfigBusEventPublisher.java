package com.penglecode.xmodule.common.cloud.synchronizer;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.penglecode.xmodule.common.cloud.consts.CloudApplicationConstants;

/**
 * 通用配置同步Bus总线事件Publisher
 * 
 * @author 	pengpeng
 * @date	2019年6月24日 下午3:44:54
 */
public abstract class GeneralConfigBusEventPublisher {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralConfigBusEventPublisher.class);
	
	@Autowired
	private ApplicationContext applicationContext;
	
	/**
	 * 同步发布Bus总线事件
	 * @param actionType
	 */
	protected void publishBusEventSync(GeneralConfigSyncRemoteBusEvent event) {
		LOGGER.info(">>> publish event，contextId = {}, originService = {}, destinationService = {}, ", applicationContext.getId(), event.getOriginService(), event.getDestinationService());
		applicationContext.publishEvent(event);
	}
	
	/**
	 * 异步发布Bus总线事件
	 * @param actionType
	 */
	protected void publishBusEventAsync(GeneralConfigSyncRemoteBusEvent event) {
		CompletableFuture.runAsync(() -> publishBusEventSync(event), CloudApplicationConstants.DEFAULT_BUS_EVENT_PUBLISH_EXECUTOR);
	}
	
}
