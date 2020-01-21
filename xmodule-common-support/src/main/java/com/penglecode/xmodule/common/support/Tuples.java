package com.penglecode.xmodule.common.support;

/**
 * 元组
 * 
 * @author 	pengpeng
 * @date	2019年12月27日 上午10:01:15
 */
public class Tuples {

	/**
	 * 二元组
	 * @param <A>
	 * @param <B>
	 * @author 	pengpeng
	 * @date	2019年12月27日 上午10:06:07
	 */
	public static class Tuple2<A, B> {
		
		private final A first;
		
		private final B second;

		public Tuple2(A first, B second) {
			super();
			this.first = first;
			this.second = second;
		}

		public A getFirst() {
			return first;
		}

		public B getSecond() {
			return second;
		}
		
	}
	
	/**
	 * 三元组
	 * @param <A>
	 * @param <B>
	 * @param <C>
	 * @author 	pengpeng
	 * @date	2019年12月27日 上午10:06:23
	 */
	public static class Tuple3<A, B, C> extends Tuple2<A, B> {

		private final C third;
		
		public Tuple3(A first, B second, C third) {
			super(first, second);
			this.third = third;
		}

		public C getThird() {
			return third;
		}
		
	}
	
	/**
	 * 四元组
	 * @param <A>
	 * @param <B>
	 * @param <C>
	 * @param <D>
	 * @author 	pengpeng
	 * @date	2019年12月27日 上午10:06:30
	 */
	public static class Tuple4<A, B, C, D> extends Tuple3<A, B, C> {

		private final D fourth;
		
		public Tuple4(A first, B second, C third, D fourth) {
			super(first, second, third);
			this.fourth = fourth;
		}

		public D getFourth() {
			return fourth;
		}

	}
	
}
