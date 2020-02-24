package com.penglecode.xmodule.hollis.examples.java.basic.oop;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * 重载(overloading) 是在一个类里面，方法名字相同，而参数不同。返回类型可以相同也可以不同。
 * 
 * 每个重载的方法（或者构造函数）都必须有一个独一无二的参数类型列表。
 * 
 * 最常用的地方就是构造器的重载。
 * 
 */
public class OverloadExample {

	public static void main(String[] args) {
		OverloadExample example = new OverloadExample();
		example.getEnvironments().forEach((key, value) -> System.out.println(key + " = " + value));
		System.out.println("-----------------------------------");
		example.getEnvironments("_HOME").forEach((key, value) -> System.out.println(key + " = " + value));
	}

	/**
	 * 获取所有环境变量
	 * @return
	 */
	public Map<String,String> getEnvironments() {
		return System.getenv();
	}
	
	/**
	 * 获取指定的环境变量
	 * @param keyContaining
	 * @return
	 */
	public Map<String,String> getEnvironments(String keyContaining) {
		return getEnvironments().entrySet().stream().filter(entry -> entry.getKey().contains(keyContaining))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
	
}
