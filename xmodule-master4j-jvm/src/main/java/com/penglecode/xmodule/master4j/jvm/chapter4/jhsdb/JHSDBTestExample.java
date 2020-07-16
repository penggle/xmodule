package com.penglecode.xmodule.master4j.jvm.chapter4.jhsdb;

/**
 * 通过实验来回答一个简单问题：staticObj、instanceObj、localObj这三个变量本身（而不是它们 所指向的对象）存放在哪里？
 * 
 * VM Args: -Xmx10m -XX:+UseSerialGC -XX:-UseCompressedOops
 * 
 * ！！！注意本例请使用JDK9及以上版本才能验证，因为JHSDB是JDK9开始才有的！！！
 * 
 * @author 	pengpeng
 * @date 	2020年7月9日 下午4:04:21
 */
public class JHSDBTestExample {

	static class Test {
		static ObjectHolder staticObj = new ObjectHolder();
		ObjectHolder instanceObj = new ObjectHolder();

		void foo() {
			ObjectHolder localObj = new ObjectHolder();
			System.out.println(localObj); // 这里设一个断点
		}
	}

	private static class ObjectHolder {
	}

	public static void main(String[] args) {
		Test test = new JHSDBTestExample.Test();
		test.foo();
	}

}
