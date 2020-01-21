package com.penglecode.xmodule.java.examples.jvm.classload;

/**
 * 类加载时机示例
 * 
 * 什么情况需要开始类加载过程的第一阶段：加载？
 * Java虚拟机规范中并没有进行强制约束，这点可以交给虚拟机的具体实现来自由把握。
 * 但是对于初始化阶段，虚拟机规范则严格规定了以下几种情况必须立即对类进行初始化，
 * 如果类没有进行过初始化，则需要先触发其初始化。
 * 
 * 1、创建类的实例，也就是new一个对象时
 * 2、访问类的静态变量时
 * 3、访问类的静态方法时
 * 4、调用Class.forName(..)方法时
 * 5、初始化一个类的子类(首先会初始化其父类)
 * 6、JVM虚拟机启动时(即定义了main方法的那个类)
 * 
 * 为了验证类加载，我们先配置一个JVM参数: -XX:+TraceClassLoading //监控类的加载
 * 
 * @author 	pengpeng
 * @date 	2020年1月21日 上午10:02:09
 */
public class ClassLoadTiming2Example {
	
	public static void main(String[] args) {
		System.out.println(ClassLoadStaticVariable.staticNum);
	}
	
}
