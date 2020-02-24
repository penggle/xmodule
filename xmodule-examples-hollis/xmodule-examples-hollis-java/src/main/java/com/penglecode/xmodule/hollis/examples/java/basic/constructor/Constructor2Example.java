package com.penglecode.xmodule.hollis.examples.java.basic.constructor;

/**
 * 继承关系下的构造函数调用规则总结：
 * 
 * 子类的构造函数中如果没有显式的调用父类(super(...))或者本类(this(...))的构造函数，
 * 那么就会默认调用父类的无参构造函数，否则不会调用父类的无参构造函数。
 */
public class Constructor2Example {

	public static void main(String[] args) {
		System.out.println("---------------new Cat()--------------");
		new Cat();
		
		System.out.println("---------------new Dog()--------------");
		new Dog();
		
		System.out.println("---------------new Dog(\"阿黄\")--------------");
		new Dog("阿黄");
		
		System.out.println("---------------new Pig()--------------");
		new Pig();
		
		System.out.println("---------------new Pig(\"八戒\")--------------");
		new Pig("八戒");
		
		System.out.println("---------------new Tiger()--------------");
		new Tiger();
		
		System.out.println("---------------new Tiger(\"大虎\")--------------");
		new Tiger("大虎");
	}

	public static class Animal {
		
		private String name;

		public Animal() {
			System.out.println("父类(Animal)无参构造函数被调用");
		}

		public Animal(String name) {
			super();
			this.name = name;
			System.out.println("父类(Animal)有参构造函数被调用, name = " + this.name);
		}

		public String getName() {
			return name;
		}

	}
	
	/**
	 * 子类继承父类的无参构造函数
	 */
	public static class Cat extends Animal {
		
	}
	
	/**
	 * 如果子类的构造函数没有显式的调用父类|本类的构造函数(via super(...)|this(...)),
	 * 那么子类的构造函数默认会调用父类的无参构造函数
	 */
	public static class Dog extends Animal {

		public Dog() {
			System.out.println("子类(Dog)无参构造函数被调用");
		}

		public Dog(String name) {
			System.out.println("子类(Dog)有参构造函数被调用, name = " + name);
		}
		
	}
	
	public static class Pig extends Animal {

		/**
		 * 子类显式调用了父类的有参构造函数，那么就不会默认再调用父类的默认无参构造函数了
		 */
		public Pig() {
			super("Pig");
			System.out.println("子类(Pig)无参构造函数被调用, name = " + getName());
		}

		public Pig(String name) {
			System.out.println("子类(Pig)有参构造函数被调用, name = " + name);
		}
		
	}
	
	public static class Tiger extends Animal {

		/**
		 * 子类显式调用了本类的有参构造函数，那么就不会默认再调用父类的默认无参构造函数了
		 */
		public Tiger() {
			this("Tiger");
			System.out.println("子类(Tiger)无参构造函数被调用, name = " + getName());
		}

		public Tiger(String name) {
			System.out.println("子类(Tiger)有参构造函数被调用, name = " + name);
		}
		
	}
	
}
