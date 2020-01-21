package com.penglecode.xmodule.common.initializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring初始化执行状态管理器
 * 
 * @author 	pengpeng
 * @date	2019年7月3日 下午1:28:26
 */
public class SpringAppInitializerStateManager {

	private static final Map<Class<? extends SpringAppInitializer>, SpringAppInitializerState> states = new HashMap<>();
	
	public static SpringAppInitializerState getState(Class<? extends SpringAppInitializer> initializerType) {
		return states.get(initializerType);
	}
	
	protected synchronized static void setState(Class<? extends SpringAppInitializer> initializerType, SpringAppInitializerState state) {
		states.put(initializerType, state);
	}
	
}
