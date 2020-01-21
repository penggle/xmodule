package com.penglecode.xmodule.common.consts;

/**
 * 常量池
 * @param <T>
 * @author 	pengpeng
 * @date	2019年6月6日 下午12:56:51
 */
public interface ConstantPool<T> {

	/**
	 * 从常量池中获取常量值
	 * @param constant
	 * @return
	 */
	public T get(Constant<T> constant);
	
}