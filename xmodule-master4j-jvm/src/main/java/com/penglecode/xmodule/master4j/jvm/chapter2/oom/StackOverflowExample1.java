package com.penglecode.xmodule.master4j.jvm.chapter2.oom;

/**
 * 通过-Xss(即-XX:ThreadStackSize)参数逐步减少栈内存大小来触发StackOverflowError测试其对栈的深度的影响。
 * 
 * 第一次设置栈内存大小为1MB：-XX:+PrintCommandLineFlags -Xss1m
 * 输出：java.lang.StackOverflowError, stack length: 18319
 * 
 * 第二次设置栈内存大小为256KB：-XX:+PrintCommandLineFlags -Xss256k
 * 输出：java.lang.StackOverflowError, stack length: 2470
 * 
 * 由上可以得出：增加栈内存大小可以增加栈的深度。
 * 
 * @author 	pengpeng
 * @date 	2020年6月18日 下午3:08:03
 */
public class StackOverflowExample1 {

	private int stackLength = 1;
	
	public void stackLeak() {
		stackLength++;
		stackLeak();
	}
	
	public static void main(String[] args) {
		StackOverflowExample1 example = new StackOverflowExample1();
		try {
			example.stackLeak();
		} catch (Throwable e) {
			System.err.println(String.format("%s, stack length: %s", e.getClass().getName(), example.stackLength));
		}
	}

}
