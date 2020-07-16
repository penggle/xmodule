package com.penglecode.xmodule.master4j.jvm.chapter3.reference;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 软引用
 * 
 * 软引用是用来描述一些还有用，但非必须的对象。只被软引用关 联着的对象，在系统将要发生内存溢出异常前，
 * 会把这些对象列进回收 范围之中进行第二次回收，如果这次回收还没有足够的内存，才会抛出内存溢出异常。
 * 在JDK 1.2版之后提供了SoftReference类来实现软引用。 
 * 
 * 一个要引用的对象不直接引用实际的对象，而是引用java中一个特定的类SoftReference，再由SoftReference类去引用要实际引用的对象。
 * 
 * @author 	pengpeng
 * @date 	2020年6月19日 下午3:53:40
 */
public class SoftReferenceExample1 {

	public static void main(String[] args) {
		Map<String,Object> person = new HashMap<>();
		person.put("name", "张三");
		person.put("age", 18);
		
		SoftReference<Map<String,Object>> personSoftRef = new SoftReference<>(person);
		System.out.println(person.get("name")); //输出"张三"
		System.out.println(personSoftRef.get().get("name")); //输出"张三"
		
		person = null; //断开强引用
		//System.gc(); //即使调用垃圾回收，下面结果也是打印"张三"，因为软引用只有系统在发生内存溢出异常之前，会把只被软引用的对象进行回收
		System.out.println(personSoftRef.get().get("name")); //输出"张三"，因为上面虽然断开了person的强引用，但是person并没有被回收
	}

}
