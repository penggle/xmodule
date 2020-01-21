package com.penglecode.xmodule.common.consts;

/**
 * Spring上下文中的bean常量
 * @param <T>
 * @author 	pengpeng
 * @date	2019年6月6日 下午12:50:52
 */
public abstract class SpringBeanConstant<T> extends Constant<T> {
	
	public SpringBeanConstant() {
		this(null, null);
	}
	
	public SpringBeanConstant(String name) {
		this(name, null);
	}
	
	public SpringBeanConstant(String name, T defaultValue) {
		super(name, defaultValue);
	}
	
}
