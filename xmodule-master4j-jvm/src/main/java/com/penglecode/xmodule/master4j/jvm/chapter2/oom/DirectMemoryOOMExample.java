package com.penglecode.xmodule.master4j.jvm.chapter2.oom;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * VM Args：-Xmx20M -XX:MaxDirectMemorySize=10M
 * 
 * @author 	zzm
 */
@SuppressWarnings("restriction")
public class DirectMemoryOOMExample {

	private static final int _1MB = 1024 * 1024;
	
	public static void main(String[] args) throws Exception {
		Field unsafeField = Unsafe.class.getDeclaredFields()[0];
		unsafeField.setAccessible(true);
		Unsafe unsafe = (Unsafe) unsafeField.get(null);
		while (true) {
			unsafe.allocateMemory(_1MB);
		}

	}

}