package com.penglecode.xmodule.master4j.jvm.chapter7.classloading.timing;

public class ClassLoadInstance {
	
	static {
		System.out.println("【ClassLoadInstance】>>> 静态块被执行了");
	}

	public ClassLoadInstance() {
		super();
		System.out.println("【ClassLoadInstance】>>> 构造函数被调用了");
	}
	
}
