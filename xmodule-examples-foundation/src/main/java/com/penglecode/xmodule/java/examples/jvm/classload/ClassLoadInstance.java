package com.penglecode.xmodule.java.examples.jvm.classload;

public class ClassLoadInstance {
	
	static {
		System.out.println("【ClassLoadInstance】>>> 静态块被执行了");
	}

	public ClassLoadInstance() {
		super();
		System.out.println("【ClassLoadInstance】>>> 构造函数被调用了");
	}
	
}
