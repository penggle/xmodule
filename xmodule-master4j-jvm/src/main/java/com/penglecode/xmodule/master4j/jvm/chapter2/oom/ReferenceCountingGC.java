package com.penglecode.xmodule.master4j.jvm.chapter2.oom;

/**
 * testGC()方法执行后，objA和objB会不会被GC呢？
 * 
 * @author 	zzm
 */
@SuppressWarnings("unused")
public class ReferenceCountingGC {

	private Object instance;
	
	private static final int _1MB = 1024 * 1024;
	
	/**
	 * 这个成员属性的唯一意义就是占点内存，以便能在GC日志中看清楚是否有回收过
	 */
    private byte[] buffer = new byte[2 * _1MB];
    
    public static void testGC() {
		ReferenceCountingGC objA = new ReferenceCountingGC();
		ReferenceCountingGC objB = new ReferenceCountingGC();
		objA.instance = objB;
		objB.instance = objA;
		objA = null;
		objB = null;
		// 假设在这行发生GC，objA和objB是否能被回收？ 
		System.gc();
    }
    
}
