package com.penglecode.xmodule.common.consts;

/**
 * Spring上下文环境变量常量
 * @param <T>
 * @author 	pengpeng
 * @date	2019年6月6日 下午12:50:52
 */
public abstract class SpringEnvConstant<T> extends Constant<T> {
	
	public SpringEnvConstant(String name) {
		this(name, null);
	}
	
	public SpringEnvConstant(String name, T defaultValue) {
		super(name, defaultValue);
	}
	
}
