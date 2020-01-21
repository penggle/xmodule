package com.penglecode.xmodule.java.examples.jvm.classload;

public class ClassLoadSingleton {

	private static ClassLoadSingleton singleton = new ClassLoadSingleton();
	
	private static int count1;
	
	private static int count2 = 0;
	
	private ClassLoadSingleton() {
		count1++;
		count2++;
		System.out.println(String.format(">>>构造器被调用了, count1 = %s, count2 = %s", count1, count2));
	}
	
	public static ClassLoadSingleton getInstance() {
		return singleton;
	}

	public static int getCount1() {
		return count1;
	}

	public static int getCount2() {
		return count2;
	}
	
}
