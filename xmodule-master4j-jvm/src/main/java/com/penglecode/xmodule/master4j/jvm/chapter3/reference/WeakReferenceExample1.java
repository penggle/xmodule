package com.penglecode.xmodule.master4j.jvm.chapter3.reference;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 软引用
 * 
 * 弱引用也是用来描述那些非必须对象，但是它的强度比软引用更弱一些，被弱引用关联的对象只能生存到下一次垃圾收集发生为止。
 * 当垃圾收集器开始工作，无论当前内存是否足够(软引用则在内存不足的情况下才会触发回收)，都会回收掉只被弱引用关联的对象。
 * 在JDK 1.2版之后提供了WeakReference类来实现弱引用。 
 * 
 * @author 	pengpeng
 * @date 	2020年6月19日 下午3:53:40
 */
public class WeakReferenceExample1 {

	/**
	 * VM args: -XX:+PrintGC
	 */
	public static void main(String[] args) {
		Map<String,Object> person = new HashMap<>();
		person.put("name", "张三");
		person.put("age", 18);
		
		WeakReference<Map<String,Object>> personWeakRef = new WeakReference<>(person);
		System.out.println(person.get("name")); //输出"张三"
		System.out.println(personWeakRef.get().get("name")); //输出"张三"
		
		System.gc(); //第一次GC
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1)); //暂停1秒等待GC
		System.out.println(personWeakRef.get().get("name")); //查看弱引用的引用对象仍然存在，未被回收！(因为上面第一句的强引用的存在)
		
		person = null; //断开强引用
		System.gc(); //调用垃圾回收，下面结果将会触发NullPointerException，因为弱引用的引用对象已经不可达(断开了强引用)，此时只要JVM发生GC，那么弱引用的引用对象就会被强制回收
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1)); //暂停1秒等待GC
		System.out.println(personWeakRef.get().get("name")); //触发NullPointerException
	}

}
