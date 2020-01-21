package com.penglecode.xmodule.java.examples.jvm.jmm;

import java.util.HashMap;
import java.util.Map;

public class FinalExample {

	/**
	 * 常量在类字节码被加载的时候初始化
	 */
	public static final Map<String,Object> props = new HashMap<String,Object>(){
		private static final long serialVersionUID = 1L; //常量
		{
			put("username", "root");
			put("password", "123456");
			System.out.println(this);
		}
	}; 
	
	private final String ip; 	//final变量
	
	private int port; 			//普通变量
	
	private static FinalExample obj;
	
	public FinalExample() { 	//构造函数
		port = 1234;			//写普通域
		ip = "localhost";		//写final域
	}
	
	/**
	 * 写final域的重排序规则：
	 * 写final域的重排序规则禁止把final域的写重排序到构造函数之外。
	 * 也就是说下面代码中在obj被指向某个引用对象时引用对象中的final域一定被初始化完毕了。
	 */
	public static void writer () {    //写线程A执行
        obj = new FinalExample ();
    }

	/**
	 * 读final域的重排序规则：
	 * 一个线程中，在读一个对象的final域之前，一定会先读包含这个final域的对象的引用。
	 * 也就是说下面语句一定先"读对象引用"再"读final域"
	 */
    @SuppressWarnings("unused")
	public static void reader () {       	//读线程B执行
        FinalExample object = obj;       	//读对象引用
        int a = object.port;               	//读普通域
        String b = object.ip;               //读final域
    }
	
	public static void main(String[] args) {
		System.out.println(FinalExample.obj);
	}

}
