package com.penglecode.xmodule.common.support;

/**
 * 单值Holder类，解决匿名内部类使用外部变量问题
 * @param <T>
 * @author 	pengpeng
 * @date	2018年2月8日 上午11:05:45
 */
public class ValueHolder<T> {

	private T value;

	public ValueHolder() {
		super();
	}

	public ValueHolder(T value) {
		super();
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
}
