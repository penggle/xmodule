package com.penglecode.xmodule.common.initializer;

/**
 * Spring初始化执行状态
 * 
 * @author 	pengpeng
 * @date	2019年7月3日 下午1:39:35
 */
public enum SpringAppInitializerState {

	UNEXECUTED, //未执行
	
	EXECUTING, //执行中
	
	EXECUTED_SUCCESS, //执行完毕(成功)
	
	EXECUTED_FAILURE //执行完毕(失败)
	
}
