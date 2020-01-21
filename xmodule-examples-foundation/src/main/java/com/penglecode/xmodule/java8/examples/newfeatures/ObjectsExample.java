package com.penglecode.xmodule.java8.examples.newfeatures;

import java.util.Objects;

public class ObjectsExample {

	private Long time;
	
	public ObjectsExample(Long time) {
		this.time = Objects.requireNonNull(time, "Parameter 'time' can not be null!");
	}
	
	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public static void main(String[] args) {
		new ObjectsExample(System.currentTimeMillis());
	}

}
