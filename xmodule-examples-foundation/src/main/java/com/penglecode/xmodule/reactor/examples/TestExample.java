package com.penglecode.xmodule.reactor.examples;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class TestExample {

	public static void test1() {
		Flux<Integer> square = Flux.range(1, 6).map(i -> i * i);
		StepVerifier.create(square)
					.expectNext(1, 4, 9, 16, 25, 36)
					.expectComplete()
					.verify();
	}
	
	public static void test2() {
		Flux<Integer> error = Flux.error(new IllegalArgumentException("args error"));
		StepVerifier.create(error)
					.expectError()
					.verify();
	}
	
	public static void test3() {
		Flux<Integer> error = Flux.error(new UnsupportedEncodingException("unsupported encoding: xyz"));
		StepVerifier.create(error)
					.expectError(IOException.class)
					.verify();
	}
	
	public static void test4() {
		Flux<Integer> error = Flux.just(1,2,3).concatWith(Mono.error(new IllegalArgumentException("args error")));
		StepVerifier.create(error)
					.expectErrorMatches(e -> {
						return true;
					})
					.verify();
	}
	
	public static void main(String[] args) {
		//test1();
		//test2();
		//test3();
		test4();
	}
	
}
