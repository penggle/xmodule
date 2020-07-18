package com.penglecode.xmodule.common.cloud.hystrix;

import java.util.HashMap;
import java.util.Map;

/**
 * Hystrix并发上下文
 * 
 * @param <T>
 * @author 	pengpeng
 * @date 	2020年2月13日 下午3:48:21
 */
public class HystrixConcurrencyContext extends HashMap<String,Object> {

	private static final long serialVersionUID = 1L;

	public HystrixConcurrencyContext() {
		super();
	}

	public HystrixConcurrencyContext(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public HystrixConcurrencyContext(int initialCapacity) {
		super(initialCapacity);
	}

	public HystrixConcurrencyContext(Map<? extends String, ?> m) {
		super(m);
	}
	
}
