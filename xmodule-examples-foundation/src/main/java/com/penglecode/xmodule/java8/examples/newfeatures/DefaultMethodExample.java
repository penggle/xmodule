package com.penglecode.xmodule.java8.examples.newfeatures;

public class DefaultMethodExample {

	public static void main(String[] args) {
		EnglishSpeaker people1 = new American();
		people1.say();
	}

	static class American implements AmericaEnglish, BritainEnglish {
		public void say() {
			AmericaEnglish.super.say();
		}
	}
	
	interface EnglishSpeaker {
		default void say() {
			System.out.println("I can say English!");
		}
	}
	
	interface BritainEnglish extends EnglishSpeaker {
		default void say() {
			EnglishSpeaker.super.say();
			System.out.println("Actually, i am say Britain English!");
		}
	}
	
	interface AmericaEnglish extends EnglishSpeaker {
		default void say() {
			EnglishSpeaker.super.say();
			System.out.println("Actually, i am say America English!");
		}
	}
	
}
