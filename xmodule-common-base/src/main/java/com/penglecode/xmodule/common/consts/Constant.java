package com.penglecode.xmodule.common.consts;

import java.lang.reflect.ParameterizedType;
import java.util.function.Supplier;

/**
 * 常量
 * @param <T>
 * @author 	pengpeng
 * @date	2019年6月6日 下午12:50:52
 */
@SuppressWarnings("unchecked")
public abstract class Constant<T> implements Supplier<T> {

	protected static ConstantPool<Object> constantPool = new DefaultEmptyConstantPool<>();
	
	protected final String name;
	
	protected final Class<T> type;
	
	protected final T defaultValue;
	
	protected Constant(String name) {
		this(name, null);
	}
	
	protected Constant(String name, T defaultValue) {
		super();
		this.defaultValue = defaultValue;
		this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.name = name;
	}
	
	/**
	 * 返回常量值
	 * @return
	 */
	public T get() {
		return (T) constantPool.get((Constant<Object>) this);
	}

	public static void setConstantPool(ConstantPool<Object> constantPool) {
		Constant.constantPool = constantPool;
	}
	
	private static class DefaultEmptyConstantPool<T> implements ConstantPool<T> {

		@Override
		public T get(Constant<T> constant) {
			return null;
		}
		
	}
	
}
