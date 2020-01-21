package com.penglecode.xmodule.java8.examples.newfeatures;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 方法引用有助于自己的名字指向方法。方法参考描述使用“::”符号。
 * 
 * 		方法所在类名::方法名
 * 
 * 一种方法参考可以用来指向下列类型的方法。
 * 		1、静态方法。
 *		2、实例方法。
 *		3、使用new运算符构造函数(TreeSet::new)。
 * @author 	pengpeng
 * @date   		2017年1月10日 下午2:35:43
 * @version 	1.0
 */
public class MethodRefExample {

	public static void main(String[] args) {
		methodRef1();
		methodRef2();
	}

	public static void methodRef1() {
		List<String> names = new ArrayList<String>();
		names.add("Mahesh");
		names.add("Suresh");
		names.add("Ramesh");
		names.add("Naresh");
		names.add("Kalpesh");
		//将java.io.PrintStream.println(Object o)方法作为forEach方法参数传入(注意目标方法的参数有且仅有一个)
		names.forEach(System.out::println);
		
		List<Integer> ages = new ArrayList<Integer>();
		ages.add(13);
		ages.add(21);
		ages.add(9);
		ages.add(34);
		ages.add(46);
		ages.add(5);
		ages.sort(Integer::compare); //方法引用
		System.out.println(ages);
	}
	
	public static void methodRef2() {
		List<Date> times = new ArrayList<Date>();
		long curTimes = System.currentTimeMillis();
		long r = 24 * 3600 * 1000;
		times.add(new Date(curTimes - (long)(Math.random() * r)));
		times.add(new Date(curTimes - (long)(Math.random() * r)));
		times.add(new Date(curTimes - (long)(Math.random() * r)));
		times.add(new Date(curTimes - (long)(Math.random() * r)));
		times.add(new Date(curTimes - (long)(Math.random() * r)));
		//将自定义的MethodRefExample.formatDate(Date date)方法作为forEach方法参数传入(注意目标方法的参数有且仅有一个)
		times.forEach(MethodRefExample::formatDate);
	}
	
	public static void formatDate(Date date) {
		String str = String.format("%tF %tT", date, date);
		System.out.println(str);
	}
	
}
