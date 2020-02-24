package com.penglecode.xmodule.hollis.examples.java.basic.autobox;

/**
 * 自动装箱: 就是将基本数据类型自动转换成对应的包装类。
 * 自动拆箱: 就是将包装类自动转换成对应的基本数据类型。
 */
public class AutoBoxing1Example {

	public void example1() {
		Integer i = 10;  //自动装箱,等价于 Integer i = Integer.valueOf(10); (反编译后可见)
	    int b = i;     //自动拆箱,等价于 int b = i.intValue(); (反编译后可见)
	    System.out.println(b);
	}
	
	public void example2() {
		Integer i = 10; //自动装箱,等价于 Integer i = Integer.valueOf(10); (反编译后可见)
		Integer j = 20; //自动装箱,等价于 Integer j = Integer.valueOf(20); (反编译后可见)
		System.out.println(i + j); //自动拆箱,等价于 System.out.println(i.intValue() + j.intValue()); (反编译后可见)
	}

}
