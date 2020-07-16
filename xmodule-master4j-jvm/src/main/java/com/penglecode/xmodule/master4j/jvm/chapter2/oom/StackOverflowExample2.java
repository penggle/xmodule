package com.penglecode.xmodule.master4j.jvm.chapter2.oom;

import java.util.UUID;

/**
 * 在栈内存大小相同情况下，通过定义局部变量的多少来触发StackOverflowError测试其对栈的深度的影响。
 * 
 * smallStackLeak()方法中未定义任何局部变量
 * 
 * largeStackLeak()方法中未定义大量局部变量
 * 
 * @author 	pengpeng
 * @date 	2020年6月18日 下午3:54:46
 */
public class StackOverflowExample2 {

	private int stackLength = 1;
	
	public void smallStackLeak() {
		stackLength++;
		smallStackLeak();
	}
	
	@SuppressWarnings("unused")
	public void largeStackLeak() {
		stackLength++;
		String a1 = UUID.randomUUID().toString();
		String a2 = UUID.randomUUID().toString();
		String a3 = UUID.randomUUID().toString();
		String a4 = UUID.randomUUID().toString();
		String a5 = UUID.randomUUID().toString();
		String a6 = UUID.randomUUID().toString();
		String a7 = UUID.randomUUID().toString();
		String a8 = UUID.randomUUID().toString();
		String a9 = UUID.randomUUID().toString();
		String a10 = UUID.randomUUID().toString();
		String a11 = UUID.randomUUID().toString();
		String a12 = UUID.randomUUID().toString();
		String a13 = UUID.randomUUID().toString();
		String a14 = UUID.randomUUID().toString();
		String a15 = UUID.randomUUID().toString();
		largeStackLeak();
	}
	
	public static void main(String[] args) {
		StackOverflowExample2 example = new StackOverflowExample2();
		try {
			//example.smallStackLeak(); // java.lang.StackOverflowError, stack length: 22968
			example.largeStackLeak(); // java.lang.StackOverflowError, stack length: 15092
		} catch (Throwable e) {
			System.err.println(String.format("%s, stack length: %s", e.getClass().getName(), example.stackLength));
		}
	}

}
