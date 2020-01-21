package com.penglecode.xmodule.java8.examples.newfeatures;

import java.util.function.BiFunction;

public class FunctionExample {

	public static void main(String[] args) {
		testFunction1();
		testFunction2();
	}
	
	public static void testFunction2() {
		BiFunction<Integer, Integer, Integer> multiply = (a, b) -> a * b;
		System.out.println("f(z)=x*y, when x=3,y=5, then f(z) = " + multiply.apply(3, 5));
	}
	
	public static void testFunction1() {
		Converter<String, Integer> converter = (source) -> Integer.parseInt(source);
		System.out.println(converter);
        Integer integer = converter.convert("012345");
        System.out.println(integer);
	}

	@FunctionalInterface
	interface Converter<S,R> {
		R convert(S source);
	}
	
}
