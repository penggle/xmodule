package com.penglecode.xmodule.master4j.jvm.chapter3.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 虚引用也称为“幽灵引用”或者“幻影引用”，它是最弱的一种引用关系。
 * 一个对象是否有虚引用的存在，完全不会对其生存时间构成影响， 也无法通过虚引用来取得一个对象实例。
 * 为一个对象设置虚引用关联的唯一目的只是为了能在这个对象被收集器回收时收到一个系统通知。
 * 在JDK 1.2版之后提供了PhantomReference类来实现虚引用。
 * 
 * 虚引用不会决定对象的生命周期，如果一个对象只有虚引用，就相当于没有引用，在任何时候都可能会被垃圾回收器回收。
 * 设置虚引用关联唯一的目的是在对象被收集器回收的时候收到一个系统通知。
 * 
 * @author 	pengpeng
 * @date 	2020年6月20日 下午4:48:38
 */
public class PhantomReferenceExample {

	/**
	 * VM args: -XX:+PrintGC
	 */
	public static void main(String[] args) {
		Map<String,Object> person = new HashMap<>();
		person.put("name", "张三");
		person.put("age", 18);
		
		ReferenceQueue<Map<String,Object>> referenceQueue = new ReferenceQueue<>();
		PhantomReference<Map<String,Object>> reference = new PhantomReference<>(person, referenceQueue);
		
		System.out.println(reference.get()); //PhantomReference#get()的实现就是永远返回null，所以此处打印null
		System.out.println(referenceQueue.poll()); //poll()是查询队列中是否有已经回收的元素，此处打印null，因为强引用person依然存在
		
		person = null; //断开强引用
		System.gc(); //触发GC来回收person对象，这时虚引用本身就会被加入到referenceQueue中
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1)); //暂停1秒等待GC
		
		System.out.println(reference.get()); //PhantomReference#get()的实现就是永远返回null，所以此处打印null
		System.out.println(referenceQueue.poll()); //poll()是查询队列中是否有已经回收的元素，此处打印出虚引用本身，因为强引用person对象被回收了
	}
	
	
}
