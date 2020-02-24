package com.penglecode.xmodule.hollis.examples.java.basic.oop;

/**
 * 多态示例
 * 
 * 多态存在的三个必要条件
 * 1、类继承或者实现接口
 * 2、子类重写了父类的方法
 * 3、父类引用指向子类对象
 * 
 */
public class PolymorphismExample {

	public static void main(String[] args) {
		Animal cat = new Cat();
		Animal dog = new Dog();
		eat(cat);
		eat(dog);
	}
	
	public static void eat(Animal animal) {
		animal.eat();
	}
	
	
	public static abstract class Animal {
		
		public abstract void eat();
		
	}
	
	public static class Cat extends Animal {

		@Override
		public void eat() {
			System.out.println("我是猫，最喜欢吃鱼");
		}
		
	}
	
	public static class Dog extends Animal {

		@Override
		public void eat() {
			System.out.println("我是狗，最喜欢吃骨头");
		}
		
	}

}
