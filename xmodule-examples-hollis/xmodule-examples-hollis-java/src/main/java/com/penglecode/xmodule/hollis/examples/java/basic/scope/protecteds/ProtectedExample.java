package com.penglecode.xmodule.hollis.examples.java.basic.scope.protecteds;

import com.penglecode.xmodule.hollis.examples.java.basic.scope.protecteds.ProtectedClass;

/**
 * protected修饰的成员变量或方法，仅同包下的类或对象可以访问，或者其子类可以访问
 */
public class ProtectedExample {

	public static void main(String[] args) {
		ProtectedClass pc = new ProtectedClass();
		System.out.println(pc.name); //仅同包下可以访问
		pc.greeting(); //仅同包下可以访问
	}

}
