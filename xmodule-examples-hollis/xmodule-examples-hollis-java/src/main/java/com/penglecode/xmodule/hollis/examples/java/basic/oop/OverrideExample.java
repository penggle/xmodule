package com.penglecode.xmodule.hollis.examples.java.basic.oop;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Java 重写(Override)示例
 * 
 * 1、访问修饰符的限制一定要不小于被重写方法的访问修饰符，比如被重写方法的访问权限是public的，那么重写方法的访问权限只能是public而不能改为更小的(例如：protected, default等)。
 * 2、参数列表必须与被重写方法完全相同。
 * 3、重写的方法的返回值必须和被重写的方法的返回值一致，或者是其子类，比如被重写方法返回值是Collection，那么重写方法的返回值可以是List或者Set。
 * 4、重写的方法所抛出的异常必须和被重写方法的所抛出的异常一致，或者是其子类。无论被重写方法是否抛出异常，重写方法都可以抛出任何UnChecked异常。被重写方法抛出异常，重写方法可以不抛出异常。
 * 5、被重写的方法不能为private，子类再写一个同名的方法并不是对父类方法进行重写(Override)，而是重新生成一个新的方法。
 * 6、静态方法不能被重写。
 * 7、不能重写被标识为final的方法，子类中必须重写父类中的abstract方法。
 * 
 */
@SuppressWarnings("unchecked")
public class OverrideExample {

	public static void main(String[] args) throws Exception {
		Animal cat = new Cat();
		System.out.println(cat.getLabels());
		
		Animal dog = new Dog();
		System.out.println(dog.getLabels());
	}

	public static abstract class Animal {
		
		protected Collection<String> getLabels() throws IOException {
			throw new IllegalStateException("No Attributes");
		}
		
		public abstract Map<String,Object> getAttributes();
		
	}
	
	public static class Cat extends Animal {

		//这里作何解释？因为：1、由于重写方法可以不抛出任何异常所以去掉IOException。2、重写方法又可以抛出任何UnCkehced Exception，故再加上IllegalStateException也是可以的。
		@Override
		public List<String> getLabels() throws IllegalStateException {
			return Arrays.asList("爱吃鱼", "专抓老鼠");
		}

		@Override
		public Map<String, Object> getAttributes() throws IllegalStateException {
			return Collections.EMPTY_MAP;
		}
		
	}
	
	public static class Dog extends Animal {

		//无论被重写方法是否抛出异常，重写方法可以抛出任何非强制性异常
		@Override
		public List<String> getLabels() throws FileNotFoundException, IllegalStateException {
			return Arrays.asList("爱吃骨头", "看家好手");
		}

		@Override
		public Map<String, Object> getAttributes() {
			return Collections.EMPTY_MAP;
		}
		
	}
	
}
