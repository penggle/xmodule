package com.penglecode.xmodule.master4j.jvm.chapter3.alloc;

/**
 * 基于Serial + Serial Old垃圾回收组合的大对象内存分配示例
 * -XX:+UseSerialGC，打开该开关后，使用Serial（年轻代）+Serial Old(老年代) 组合进行GC
 * 
 * @author 	pengpeng
 * @date 	2020年7月6日 下午4:43:01
 */
public class AllocationExample {

	private static final int _1MB = 1024 * 1024;
	
	/**
	 * 测试大对象的分配
	 * VM Args：-verbose:gc -Xmx20M -Xms20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:+UseSerialGC
	 */
	@SuppressWarnings("unused")
	public static void testAllocation1() {
		byte[] allocation1, allocation2, allocation3, allocation4;
		allocation1 = new byte[2 * _1MB];
		allocation2 = new byte[2 * _1MB];
		allocation3 = new byte[2 * _1MB];
		allocation4 = new byte[4 * _1MB]; // 出现一次Minor GC
	}
	
	/**
	 * 测试大对象的分配
	 * VM Args：-verbose:gc -Xmx20M -Xms20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:+UseSerialGC -XX:PretenureSizeThreshold=3145728
	 */
	@SuppressWarnings("unused")
	public static void testAllocation2() {
		byte[] allocation = new byte[4 * _1MB]; //直接分配在老年代中
	}
	
	public static void main(String[] args) {
		//testAllocation1();
		testAllocation2();
	}

}
