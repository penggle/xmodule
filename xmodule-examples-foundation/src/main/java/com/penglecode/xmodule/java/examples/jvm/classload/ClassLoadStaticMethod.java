package com.penglecode.xmodule.java.examples.jvm.classload;

public class ClassLoadStaticMethod {
	
	static {
		System.out.println("【ClassLoadStaticMethod】>>> 静态块被执行了");
	}
	
	public static void staticMethod() {
		System.out.println("【ClassLoadStaticMethod】>>> 静态方法被执行了");
	}

}
