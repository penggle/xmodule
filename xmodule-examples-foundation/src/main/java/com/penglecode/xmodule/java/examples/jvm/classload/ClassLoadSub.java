package com.penglecode.xmodule.java.examples.jvm.classload;

public class ClassLoadSub extends ClassLoadSuper {

	public static int subId = 1;
	
	static {
		System.out.println("【ClassLoadSub】>>> 子类静态块被执行了");
	}

	public ClassLoadSub() {
		System.out.println("【ClassLoadSub】>>> 子类构造函数被调用了");
	}
	
}
