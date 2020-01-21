package com.penglecode.xmodule.java.examples.jvm.jmm;

public class FinalFieldExample {
	final int x;
	int y;
	static FinalFieldExample f;

	public FinalFieldExample() {
		x = 3;
		y = 4;
	}

	static void writer() {
		f = new FinalFieldExample();
	}

	/**
	 * 一个正在执行reader方法的线程保证看到f.x的值为3，因为它是final字段。
	 * 它不保证看到f.y的值为4，因为f.y不是final字段。
	 */
	@SuppressWarnings("unused")
	static void reader() {
		if (f != null) {
			int i = f.x;
			int j = f.y;
		}
	}
}
