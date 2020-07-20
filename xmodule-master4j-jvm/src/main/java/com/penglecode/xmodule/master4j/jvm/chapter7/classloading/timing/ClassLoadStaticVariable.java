package com.penglecode.xmodule.master4j.jvm.chapter7.classloading.timing;

public class ClassLoadStaticVariable {
	
	static {
		System.out.println("【ClassLoadStaticVariable】>>> 静态块被执行了");
	}
	
	public static int staticNum = 1;

}
