package com.penglecode.xmodule.master4j.java.util.stream;

import java.util.Arrays;
import java.util.List;

public class CollectorExample1 {

	/**
	 * 求和例子
	 * 在串行流中，Collector仅会执行初始化器supplier，累加器accumulator
	 * 合并器combiner不会执行
	 */
	public static void syncCollect4Sum() {
		List<Integer> numbers = Arrays.asList(-3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		ValueHolder<Integer> sum = numbers.stream()
                .filter(i -> i > 0)
                .collect(() -> {
                	ValueHolder<Integer> supplier = new ValueHolder<Integer>(0);
                	System.out.println("【" + Thread.currentThread().getName() + "】supplier : " + supplier);
                	return supplier;
                }, (s, e) -> {
                	System.out.println("【" + Thread.currentThread().getName() + "】accumulator : " + s);
                	s.setValue(s.getValue() + e);
                }, (s1, s2) -> {
                	System.out.println("【" + Thread.currentThread().getName() + "】combiner : s1 = " + s1 + ", s2 = " + s2);
                	s1.setValue(s1.getValue() + s2.getValue());
                });
		System.out.println("【" + Thread.currentThread().getName() + "】sum = " + sum);
        System.out.println(sum.getValue());
	}
	
	/**
	 * 求和例子
	 * 在并行流中合并器combiner会对任务小计进行合并最终得出最终结果 (类似于ForkJoin)
	 */
	public static void asyncCollect4Sum() {
		List<Integer> numbers = Arrays.asList(-3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		ValueHolder<Integer> sum = numbers.parallelStream()
                .filter(i -> i > 0)
                .collect(() -> {
                	ValueHolder<Integer> supplier = new ValueHolder<Integer>(0);
                	System.out.println("【" + Thread.currentThread().getName() + "】supplier : " + supplier);
                	return supplier;
                }, (s, e) -> {
                	System.out.println("【" + Thread.currentThread().getName() + "】accumulator : " + s);
                	s.setValue(s.getValue() + e);
                }, (s1, s2) -> {
                	System.out.println("【" + Thread.currentThread().getName() + "】combiner : s1 = " + s1 + ", s2 = " + s2);
                	s1.setValue(s1.getValue() + s2.getValue());
                });
		System.out.println("【" + Thread.currentThread().getName() + "】sum = " + sum);
        System.out.println(sum.getValue());
	}
	
	public static void main(String[] args) {
		syncCollect4Sum();
		asyncCollect4Sum();
	}
	
	public static class ValueHolder<T> {
		
		private T value;

		public ValueHolder() {
			super();
		}

		public ValueHolder(T value) {
			super();
			this.value = value;
		}

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}
		
	}

}
