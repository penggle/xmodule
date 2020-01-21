package com.penglecode.xmodule.java.examples.jvm.jmm;

/**
 * Volatile字段是用于线程间通讯的特殊字段。每次读volatile字段都会看到其它线程写入该字段的最新值，
 * 同样一个线程获得锁之后进入同步块时，同步块中的共享变量将会失效本地缓存从主存中load它的最新值，离开同步块时将会将修改的最新值刷新到主存中去。
 * 从原子性，可见性和有序性的角度分析，声明为volatile字段的作用相当于一个类通过get/set同步方法保护普通字段，如下：
 * 
 * @author 	pengpeng
 * @date	2017年10月27日 下午1:10:50
 */
public class VolatileAchieveExample {

	private int value;
	
	public synchronized int get() {
		return value;
	}
	
	public synchronized void set(int value) {
		this.value = value;
	}
	
}
