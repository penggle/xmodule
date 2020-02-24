package com.penglecode.xmodule.hollis.examples.java.basic.constructor;

public class MyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		throw new MyException(); //构造函数不具备继承特性
	}
	
}
