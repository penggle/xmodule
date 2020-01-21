package com.penglecode.xmodule.java.examples.concurrent.sync;

/**
 * 基于JDK1.7测试
 * 
 * 并发强度大的时候,Lock表现出来的性能要比synchronized好,
 * 反之,并发强度低的时候,synchronized表现出来的性能要比lock好,
 * 
 * @author  pengpeng
 * @date 	 2014年5月21日 下午2:42:51
 * @version 1.0
 */
public class SynchronizationPerformanceExample extends SynchronizationPerformanceTestTemplate {

	public static void main(String[] args) throws Exception {
		SynchronizationPerformanceExample example = new SynchronizationPerformanceExample();
		//example.test4Synchronized(10000, 10000);
		//example.test4Lock(10000, 10000);
		example.test4Atomic(10000, 10000);
	}

}