package com.penglecode.xmodule.master4j.jvm.chapter3.reference;

/**
 * 强引用
 * 
 * 强引用是最传统的“引用”的定义，是指在程序代码之中普遍存在的引用赋值，即类似“Object obj=new Object()”这种引用关系。
 * 无论任何情况下，只要强引用关系还存在，垃圾收集器就永远不会回收掉被引用的对象。
 * 
 * @author 	pengpeng
 * @date 	2020年6月19日 下午3:50:43
 */
public class StrongReferenceExample {

	public static void main(String[] args) {
		Object obj = new Object(); //强引用
		System.out.println(obj);
	}

}
