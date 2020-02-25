package com.penglecode.xmodule.hollis.examples.java.basic.autobox;

public class AutoBoxingExample2 {

	public static void testInteger1() {
		int a1 = 1234567890;
		Integer b1 = a1;
		System.out.println(b1 == a1); //由于==运算符存在一方为原始类型，所以此处涉及自动拆箱，等价于(b1.intValue() == a1)，所以此处肯定输出true
		System.out.println(b1.equals(a1)); //equals没什么好讲的，肯定也是输出true
	}
	
	public static void testInteger2() {
		Integer a1 = 123456;
		Integer b1 = a1; //a1的引用赋给了b1，那么a1,b1的引用地址肯定一样的
		System.out.println(b1 == a1); //由于a1的引用赋给了b1，且==运算符两边皆是Object对象，所以比较的是引用地址的一致性，所以输出true
		System.out.println(b1.equals(a1));
	}
	
	public static void testInteger3() {
		Integer a1 = 123; //自动装箱: a1 = Integer.valueOf(123);
		Integer b1 = 123; //自动装箱: b1 = Integer.valueOf(123);
		/**
		 * a1 == b1运算符两端皆是Object对象，因此比较的是引用地址，进入#Integer.valueOf(int i)方法可知：
		 * 存在IntegerCache[-128,127]缓存，如果装箱值在这个范围内那么返回缓存的Integer对象，因此引用地址肯定相同，
		 * 下面代码中当a1,b1的值超过127时，那么就不走缓存了，因此是new出一个新的对象，引用地址肯定就不一样了
		 * 
		 * 同样的缓存特性一样存在于Boolean,Character,Byte,Short,Integer,Long对象中
		 */
		System.out.println(a1 == b1);
		
		a1 = 127;
		b1 = 127;
		System.out.println(a1 == b1);
		
		a1 = 128;
		b1 = 128;
		System.out.println(a1 == b1);
	}
	
	public static void main(String[] args) {
		//testInteger1();
		//testInteger2();
		testInteger3();
	}

}
