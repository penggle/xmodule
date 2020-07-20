package com.penglecode.xmodule.master4j.jvm.chapter7.classloading.timing;

public class ClassLoadSuper {

	public static int superId = 1;
	
	static {
		System.out.println("【ClassLoadSuper】>>> 父类静态块被执行了");
	}

	public ClassLoadSuper() {
		System.out.println("【ClassLoadSuper】>>> 父类构造函数被调用了");
	}
	
}
