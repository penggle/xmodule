package com.penglecode.xmodule.hollis.examples.java.basic.constructor;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Java构造函数
 * 
 * 1、如果一个类不包含构造函数声明，那么隐式声明一个没有形式参数且没有throws子句的默认构造函数，如果在该类中自定义了构造函数，那么默认构造函数就没有了。
 * 2、构造函数也是函数的一种，同样具备函数的重载（Overloding）特性。
 * 3、构造函数不像普通函数那样具有返回值。
 * 4、构造函数也可以声明抛出异常。
 * 5、构造函数不具备继承特性，即如果父类有多个构造函数，子类没有构造函数，那么子类也不会继承父类的多个构造函数，而是仅有一个默认的无参构造函数。
 * 6、类中存在两种特殊的代码块，即非静态代码块和静态代码块，前者是直接由 { } 括起来的代码，而后者是由 static{ } 括起来的代码。
 * 	  非静态代码块在类初始化创建实例时，将会被提取到类的构造器中执行，但是非静态代码块会比构造器中的代码块先被执行。
 * 
 */
public class Constructor1Example {

	private static int a;
	
	private URL url;
	
	/**
	 * 非静态块在类初始化(new)时执行，非静态块将会被提取到构造器中执行且在构造器代码之前执行。
	 */
	{
		System.out.println("非静态块, url = " + url);
		//非静态块中可以调用静态变量，反之不行。这个跟静态方法中不能调用成员变量一个道理。
		System.out.println("非静态块, a = " + a);
	}
	
	public Constructor1Example() {
		System.out.println("自定义的无参构造函数");
	}
	
	/**
	 * 构造函数也可以声明抛出异常
	 */
	public Constructor1Example(String url) throws MalformedURLException {
		System.out.println("带参构造函数");
		this.url = new URL(url);
		System.out.println(this.url);
	}

	public URL getUrl() {
		return url;
	}
	
	public static void main(String[] args) throws Exception {
		new Constructor1Example("http://www.baidu.com");
	}

}