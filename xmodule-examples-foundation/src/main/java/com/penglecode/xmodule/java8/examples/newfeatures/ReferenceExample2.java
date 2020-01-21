package com.penglecode.xmodule.java8.examples.newfeatures;

public class ReferenceExample2 {

	public void process1(int a) {
		System.out.println("【1】>>> " + a);
		a = 456;
		System.out.println("【2】>>> " + a);
	}
	
	public void process2(int a) {
		System.out.println("【3】>>> " + a);
	}
	
	public static void main(String[] args) {
		ReferenceExample2 example = new ReferenceExample2();
		int a = 123;
		example.process1(a);
		example.process2(a);
	}

}
