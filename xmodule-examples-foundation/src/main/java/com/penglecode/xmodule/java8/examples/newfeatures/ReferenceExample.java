package com.penglecode.xmodule.java8.examples.newfeatures;
/**
 * 引用传递示例
 * 
 * @author 	pengpeng
 * @date	2017年1月27日 下午7:31:23
 */
public class ReferenceExample {

	private String name;
	
	public ReferenceExample(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void process1(ReferenceExample example) {
		System.out.println("【1】>>> " + example);
		example = new ReferenceExample("张三");
		System.out.println("【2】>>> " + example);
	}
	
	public void process2(ReferenceExample example) {
		System.out.println("【3】>>> " + example);
	}
	
	public static void main(String[] args) {
		ReferenceExample example = new ReferenceExample("李四");
		example.process1(example);
		example.process2(example);
	}

}
