package com.penglecode.xmodule.master4j.jvm.chapter7.classloading.timing;

public class ClassLoadStaticMethod {
	
	static {
		System.out.println("【ClassLoadStaticMethod】>>> 静态块被执行了");
	}
	
	public static void staticMethod() {
		System.out.println("【ClassLoadStaticMethod】>>> 静态方法被执行了");
	}

}
