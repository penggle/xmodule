package com.penglecode.xmodule.java.examples.jvm.jmm;

/**
 * 重排序：编译器、处理器在不影响程序正确性的情况下会允许做一些语句或指令的重排序，以优化程序的执行效率。
 * 
 * @author 	pengpeng
 * @date	2017年10月27日 下午2:24:48
 */
public class CodeReorderingExample {

	private int x = 0;
	
	private int y = 1;
	
	/**
	 * 由于x，y变量之间没有数据依赖性，且xy没有被volatile关键字修饰，
	 * 所以处理器在执行以下语句的时候可能以优化的名义对其进行重排序
	 */
	public void write() {
		x = 1;
		y = 2;
	}
	
	/**
	 * 在多线程环境下，当r1读到y的值为2时，r2读到x的值却不一定为1，因为存在重排序
	 */
	@SuppressWarnings("unused")
	public void read() {
		int r1 = y;
		int r2 = x; //当r1读到y的值为2时，r2读到x的值却不一定为1，因为存在重排序
	}
	
}
