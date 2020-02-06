package com.penglecode.xmodule.reactor.examples;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class EmptyExample {

	/**
	 * 在Flux<T>上调用single系列方法必须确保Flux<T>流中有且仅有一个元素,否则报错：java.lang.IndexOutOfBoundsException: Source emitted more than one item
	 * (如果Flux<T>为空，那么调用single(defaultValue)及singleOrEmpty()方法将不会报错)
	 */
	public static void fineIfOnlyOneElement1() {
		Flux<String> langs = Flux.just("java", "c++", "go", "ruby", "python", "nodejs");
		langs.single().subscribe(System.out::println);
	}
	
	/**
	 * 在Flux<T>上调用single系列方法必须确保Flux<T>流中有且仅有一个元素,否则报错：java.lang.IndexOutOfBoundsException: Source emitted more than one item
	 * (如果Flux<T>为空，那么调用single(defaultValue)及singleOrEmpty()方法则不会报错)
	 */
	public static void fineIfOnlyOneElement2() {
		Flux<String> langs = Flux.just("java", "c++", "go", "ruby", "python", "nodejs");
		langs.filter(l -> l.equals("c")).single("NotFound").subscribe(System.out::println);
	}
	
	/**
	 * 此例没有switchIfEmpty处理分支，因此在没有找到指定条件的元素时什么也不做，因为没有可用元素推送给订阅者
	 */
	public static void fineIfOnlyOneElement3() {
		Flux<String> langs = Flux.just("java", "c++", "go", "ruby", "python", "nodejs");
		langs.filter(l -> l.equals("c")).singleOrEmpty().subscribe(System.out::println);
	}
	
	/**
	 * 此例使用switchIfEmpty分支来在元素未找到时做一些操作
	 */
	public static void fineIfOnlyOneElement4() {
		Flux<String> langs = Flux.just("java", "c++", "go", "ruby", "python", "nodejs");
		langs.filter(l -> l.equals("c")).singleOrEmpty().switchIfEmpty(Mono.defer(() -> {
			return Mono.just("NotFound");
		})).subscribe(System.out::println);
	}
	
	/**
	 * 诸如map(), flatMap这些Operator，都永远不会出现类似NullPointerException，
	 * 因为如果没有找到元素，那么无元素可推送因此，在无元素可用时压根就不会真正执行这些Operator
	 */
	public static void findMyLikeOne() {
		Flux<String> langs = Flux.just("java", "c++", "go", "ruby", "python", "nodejs");
		langs.filter(l -> l.equals("c")).map(l -> String.format("my favourite language is: %s", l)).subscribe(System.out::println);
	}
	
	public static void handleEmpty1() {
		Mono<String> c = findLanguage1(Mono.just("c"));
		c.subscribe(System.out::println);
	}
	
	public static void handleEmpty2() {
		Mono<String> c = findLanguage1(Mono.empty());
		System.out.println(c);
		c.subscribe(System.out::println);
	}
	
	public static void handleEmpty3() {
		Mono<String> c = findLanguage2(Mono.empty());
		System.out.println(c);
		c.subscribe(System.out::println);
	}
	
	private static Mono<String> findLanguage1(Mono<String> query) {
		Flux<String> langs = Flux.just("java", "c++", "go", "ruby", "python", "nodejs");
		return query.flatMap(q -> {
			System.out.println(">>> q = " + q);
			return langs.filter(l -> l.equals(q)).single("NotFound");
		});
	}
	
	private static Mono<String> findLanguage2(Mono<String> query) {
		Flux<String> langs = Flux.just("java", "c++", "go", "ruby", "python", "nodejs");
		return query.flatMap(q -> {
			System.out.println(">>> q = " + q);
			return langs.filter(l -> l.equals(q)).single("NotFound");
		}).switchIfEmpty(Mono.defer(() -> Mono.empty()));
	}
	
	public static void main(String[] args) {
		//fineIfOnlyOneElement1();
		//fineIfOnlyOneElement2();
		//fineIfOnlyOneElement3();
		//fineIfOnlyOneElement4();
		//findMyLikeOne();
		//handleEmpty1();
		handleEmpty2();
		handleEmpty3();
	}

}
