package com.penglecode.xmodule.hollis.examples.java.basic.scope;

import com.penglecode.xmodule.hollis.examples.java.basic.scope.defaults.DefaultsClass;

/**
 * default:默认修饰的成员变量或方法，仅同包下的类或对象可以访问，子类也不可以
 */
public class DefaultsExample extends DefaultsClass {
	
	protected void hello() {
		//System.out.println("hello " + name); //子类不可访问
		//greeting(); //子类不可访问
	}

	public static void main(String[] args) {
		DefaultsClass dc = new DefaultsClass();
		System.out.println(dc);
		//System.out.println(dc.name); //仅同包下可以访问，子包下的不可访问
		//dc.greeting(); //仅同包下可以访问，子包下的不可访问
	}

}
