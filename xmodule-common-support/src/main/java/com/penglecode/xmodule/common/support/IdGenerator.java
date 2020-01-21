package com.penglecode.xmodule.common.support;

import java.util.List;

import org.springframework.core.NestedRuntimeException;

/**
 * 全局唯一主键生成器
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月17日 下午8:06:15
 * @version  	1.0
 */
public interface IdGenerator<T> {

	/**
	 * 生成一个主键
	 * @return
	 */
	public T nextId();
	
	/**
	 * 一次生成多个主键
	 * @return
	 */
	public List<T> nextIds(int batchSize);
	
	public static class KeyGeneratorException extends NestedRuntimeException {

		private static final long serialVersionUID = 1L;

		public KeyGeneratorException(String msg) {
			super(msg);
		}

		public KeyGeneratorException(String msg, Throwable cause) {
			super(msg, cause);
		}
		
	}
	
}
