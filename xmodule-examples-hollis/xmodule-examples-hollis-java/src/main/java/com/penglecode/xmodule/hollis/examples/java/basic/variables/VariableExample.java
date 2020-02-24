package com.penglecode.xmodule.hollis.examples.java.basic.variables;

/**
 * Java中变量分为：
 * 1、静态变量，分配在JVM的方法区
 * 2、成员变量，分配在JVM的堆内存区域
 * 3、局部变量，分配在JVM的栈内存区域
 */
public class VariableExample {

	//静态变量，分配在JVM的方法区
	private static int a = 1;
	
	//成员变量，分配在JVM的堆内存区域
	private int b;
	
	/**
	 * x, y, f都是局部变量，分配在JVM的栈内存区域
	 */
	public int compute(int x, int y) {
		int f = a * b;
		return (x + y) * f;
	}
	
}
