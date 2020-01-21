package com.penglecode.xmodule.java8.examples.newfeatures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.penglecode.xmodule.java8.examples.model.StudentScore;

public class ComparatorExample {

	private static final List<StudentScore> ALL_STUDENTSCORE_LIST = new ArrayList<StudentScore>();
	
	static {
		ALL_STUDENTSCORE_LIST.add(new StudentScore("张三", "语文", 78.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("张三", "数学", 89.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("张三", "英语", 66.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("李四", "语文", 67.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("李四", "数学", 92.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("李四", "英语", 56.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("王五", "语文", 96.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("王五", "数学", 58.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("王五", "英语", 89.0));
	}
	
	/**
	 * 按分数排名
	 */
	public static void compare1() {
		Collections.sort(ALL_STUDENTSCORE_LIST, Comparator.comparing(StudentScore::getScore).reversed());
		ALL_STUDENTSCORE_LIST.forEach(System.out::println);
	}
	
	/**
	 * 按课程、分数排名
	 */
	public static void compare2() {
		Collections.sort(ALL_STUDENTSCORE_LIST, Comparator.comparing(StudentScore::getCourse).thenComparing(StudentScore::getScore).reversed());
		ALL_STUDENTSCORE_LIST.forEach(System.out::println);
	}
	
	/**
	 * 排序，且考虑属性值为null的情况
	 */
	public static void compare3() {
		List<User> userList = new ArrayList<User>();
		userList.add(new User("zhangsan"));
		userList.add(new User("lisi"));
		userList.add(new User(null));
		userList.add(new User("wangwu"));
		userList.add(new User("hanliu"));
		
		//以下两种写法等效
		userList.stream().sorted(Comparator.comparing(User::getName, Comparator.nullsLast(Comparator.naturalOrder()))).forEach(System.out::println);
		
		//userList.stream().sorted((o1, o2) -> Objects.compare(o1.getName(), o2.getName(), Comparator.nullsLast(Comparator.naturalOrder()))).forEach(System.out::println);
	}
	
	public static void main(String[] args) {
		//compare1();
		compare2();
	}

	public static class User {
		
		private String name;

		public User(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "User [name=" + name + "]";
		}
		
	}
	
}
