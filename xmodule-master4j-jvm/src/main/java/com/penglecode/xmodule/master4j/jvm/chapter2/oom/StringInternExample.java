package com.penglecode.xmodule.master4j.jvm.chapter2.oom;

/**
 * 关于字符串常量池的示例
 * 
 * 在JDK 6中，intern()方法 会把首次遇到的字符串实例复制到永久代的字符串常量池中存储，返回 的也是永久代里面这个字符串实例的引用
 * 
 * 而JDK 7（以及部分其他虚拟机，例如JRockit）的intern()方法实现就不需要再拷贝字符串的实例到永久代了，既然字符串常量池已经移到Java堆中，
 * 那只需要在常量池里记录一下首次出现的实例引用即可
 * 
 * @author 	pengpeng
 * @date 	2020年6月18日 下午5:29:28
 */
public class StringInternExample {

	public static void main(String[] args) {
		String str1 = new StringBuilder("计算机").append("软件").toString(); //编译期优化，等价于：String str1 = new String("计算机软件");
		/**
		 * JDK6及以下版本，调用intern()方法时会把首次出现的字符串"计算机软件"拷贝到永久代中的字符串常量池中，然后返回永久代里面这个字符串实例的引用
		 * 而str1对象实例是在堆中的，所以一个在永久代一个在堆上，所以输出false
		 * 
		 * JDK7及以上版本，调用intern()方法时就不需要再拷贝字符串的实例到永久代了，而是直接返回其在堆中常量池中的引用即可，
		 * 因此intern()返回的引用与str1是同一个，所以输出true
		 */
		System.out.println(str1.intern() == str1);
        String str2 = new StringBuilder("ja").append("va").toString(); //编译期优化，等价于：String str1 = new String("java");
        
        /**
         * 所有版本都输出false，因为"java"这个字符串是个特殊的字符串，在JVM启动时由sun.misc.Version这个类放入常量池的，已经不是首次出现了，
         * 所以肯定为false
         */
        System.out.println(str2.intern() == str2);
	}

}
