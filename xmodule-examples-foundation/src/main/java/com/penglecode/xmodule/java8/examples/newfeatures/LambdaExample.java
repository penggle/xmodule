package com.penglecode.xmodule.java8.examples.newfeatures;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Lambda表达式是在Java8中引入的，并号称是Java8的最大的特点. 
 * Lambda表达式有利于函数式编程，简化了开发了很多。
 * 
 * 语法
 *		lambda表达式的特点，它的语法如下面:
 *		parameter -> expression body
 *
 * 下面是一个lambda表达式的重要特征。
 *
 *		1、可选类型声明 - 无需声明参数的类型。编译器可以从该参数的值推断。
 *		2、可选圆括号参数 - 无需在括号中声明参数。对于多个参数，括号是必需的。
 *		3、可选大括号 - 表达式主体没有必要使用大括号，如果主体中含有一个单独的语句。
 *		4、可选return关键字 - 编译器会自动返回值，如果主体有一个表达式返回的值。花括号是必需的，以表明表达式返回一个值。
 * 
 * @author 	pengpeng
 * @date   		2017年1月10日 上午9:02:55
 * @version 	1.0
 */
public class LambdaExample {

	public static void main(String[] args) {

		/*
		 //以下涉及到函数式接口：类似于接口匿名实现类
		 //Lambda表达式消除匿名类的需求，并给出了一个非常简单但功能强大的函数式编程能力。
		MathOperation addition = new MathOperation(){
			public int operation(int a, int b) {
				return a + b;
			}
		};*/
		
		// 声明了参数的类型(可选)
		MathOperation addition = (int a, int b) -> a + b; //简写形式,如果函数主体只有一个表达式
		System.out.println(addition);
		//省略了参数类型的声明
		MathOperation subtraction = (a, b) -> a - b;
		System.out.println(subtraction);
		//全写形式
		MathOperation multiplication = (int a, int b) -> {return a * b;};
		System.out.println(multiplication);
		//省略了参数类型的声明
		MathOperation division = (a, b) -> {return a / b;};
		System.out.println(division);
		System.out.println("10 + 5 = " + addition.operation(10, 5));
		System.out.println("10 - 5 = " + subtraction.operation(10, 5));
		System.out.println("10 * 5 = " + multiplication.operation(10, 5));
		System.out.println("10 / 5 = " + division.operation(10, 5));
		
		//单个入参可以省略参数括号
		GreetingService greetingService1 = message -> System.out.println("Hello, " + message);
		System.out.println(greetingService1);
		greetingService1.sayMessage("Java8");
		GreetingService greetingService2 = (message) -> System.out.println("Hello! " + message);
		System.out.println(greetingService2);
		greetingService2.sayMessage("Java8");
		
		List<String> names = Arrays.asList("Mahesh", "Suresh", "Ramesh", "Naresh", "Kalpesh");
		System.out.println(names);
		Collections.sort(names, (s1, s2) -> s1.compareTo(s2));
		System.out.println(names);
	}
	
	interface MathOperation {
		int operation(int a, int b);
	}
	
	interface GreetingService {
		void sayMessage(String message);
	}

}
