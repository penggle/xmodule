package com.penglecode.xmodule.common.cloud.synchronizer;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * 通用的配置同步Bus总线事件
 * 
 * @author 	pengpeng
 * @date	2019年6月19日 下午1:58:56
 */
@SuppressWarnings("serial")
public abstract class GeneralConfigSyncRemoteBusEvent extends RemoteApplicationEvent {

	private SyncActionType actionType;
	
	public GeneralConfigSyncRemoteBusEvent() {
		super();
	}
	
	public GeneralConfigSyncRemoteBusEvent(SyncActionType actionType) {
		super();
		this.actionType = actionType;
	}

	public GeneralConfigSyncRemoteBusEvent(Object source, String originService, SyncActionType actionType) {
		super(source, originService);
		this.actionType = actionType;
	}

	public GeneralConfigSyncRemoteBusEvent(Object source, String originService, String destinationService, SyncActionType actionType) {
		super(source, originService, destinationService);
		this.actionType = actionType;
	}

	public SyncActionType getActionType() {
		return actionType;
	}

	public void setActionType(SyncActionType actionType) {
		this.actionType = actionType;
	}
	
}
