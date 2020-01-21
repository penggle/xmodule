package com.penglecode.xmodule.java8.examples.newfeatures;

import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConsumerExample {

	public static void main(String[] args) {
		testConsumer1();
	}
	
	public static void testConsumer1() {
		Consumer<Object> printer1 = System.out::println;
		printer1.accept("现在时间是：" + new Date());
		
		//使用Function实现
		Function<Object,Object> printer2 = o -> {
			System.out.println(o);
			return o;
		};
		printer2.apply("现在时间是：" + new Date());
	}

}
