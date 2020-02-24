package com.penglecode.xmodule.hollis.examples.java.basic.datatype;

public class JavaDataTypeExample {

	private char c;
	
	private boolean bool;
	
	private byte b;
	
	private short s;
	
	private int i;
	
	private long l;
	
	private float f;
	
	private double d;
	
	public void chars() {
		System.out.println("char的默认值：" + c);
		System.out.println("char的最大值：" + Character.MAX_VALUE);
		System.out.println("char的最小值：" + Character.MIN_VALUE);
		System.out.println(c == Character.MIN_VALUE);
		
		String s = "abc";
		
		int cp = s.codePointAt(0);
		System.out.println(cp);
		System.out.println(cp == 'a');
	}
	
	public void booleans() {
		System.out.println("boolean的默认值：" + bool);
	}
	
	public void bytes() {
		System.out.println("byte的默认值：" + b);
		System.out.println("byte的最大值：" + Byte.MAX_VALUE);
		System.out.println("byte的最小值：" + Byte.MIN_VALUE);
	}
	
	public void shorts() {
		System.out.println("short的默认值：" + s);
		System.out.println("short的最大值：" + Short.MAX_VALUE);
		System.out.println("short的最小值：" + Short.MIN_VALUE);
	}
	
	public void ints() {
		System.out.println("int的默认值：" + i);
		System.out.println("int的最大值：" + Integer.MAX_VALUE);
		System.out.println("int的最小值：" + Integer.MIN_VALUE);
	}
	
	public void longs() {
		System.out.println("long的默认值：" + l);
		System.out.println("long的最大值：" + Long.MAX_VALUE);
		System.out.println("long的最小值：" + Long.MIN_VALUE);
	}
	
	public void floats() {
		System.out.println("float的默认值：" + f);
		System.out.println("float的最大值：" + Float.MAX_VALUE);
		System.out.println("float的最小值：" + Float.MIN_VALUE);
	}
	
	public void doubles() {
		System.out.println("double的默认值：" + d);
		System.out.println("double的最大值：" + Double.MAX_VALUE);
		System.out.println("double的最小值：" + Double.MIN_VALUE);
		
		System.out.println((long)Double.MAX_VALUE);
		System.out.println(Double.MAX_VALUE > Long.MAX_VALUE);
	}
	
	public static void main(String[] args) {
		JavaDataTypeExample example = new JavaDataTypeExample();
		System.out.println("--------------------chars-------------------");
		example.chars();
		System.out.println("--------------------booleans-------------------");
		example.booleans();
		System.out.println("--------------------bytes-------------------");
		example.bytes();
		System.out.println("--------------------shorts-------------------");
		example.shorts();
		System.out.println("--------------------ints-------------------");
		example.ints();
		System.out.println("--------------------longs-------------------");
		example.longs();
		System.out.println("--------------------floats-------------------");
		example.floats();
		System.out.println("--------------------doubles-------------------");
		example.doubles();
	}

}
