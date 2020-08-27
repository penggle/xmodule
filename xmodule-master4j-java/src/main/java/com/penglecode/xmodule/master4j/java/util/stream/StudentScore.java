package com.penglecode.xmodule.master4j.java.util.stream;

public class StudentScore {

	private String name;
	
	private String course;
	
	private Double score;

	public StudentScore() {
		super();
	}

	public StudentScore(String name, String course, Double score) {
		super();
		this.name = name;
		this.course = course;
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "StudentScore [name=" + name + ", course=" + course + ", score=" + score + "]";
	}
	
}
