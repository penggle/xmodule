package com.penglecode.xmodule.master4j.jvm.chapter7.classloading.timing;

public class ClassLoadSingletonExample {

	public static void main(String[] args) {
		System.out.println("count1 = " + ClassLoadSingleton.getCount1()); //按顺序执行 public static int count1; 没有赋值，所以count1依旧为1;
		System.out.println("count2 = " + ClassLoadSingleton.getCount2()); //按顺序执行 public static int count2 = 0;被赋值了，所以count2变为0;
	}

}
