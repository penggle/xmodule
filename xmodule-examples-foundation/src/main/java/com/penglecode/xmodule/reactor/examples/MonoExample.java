package com.penglecode.xmodule.reactor.examples;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Mono 表示的是包含 0 或者 1 个元素的异步序列。该序列中同样可以包含与 Flux 相同的三种类型的消息通知。
 * Flux 和 Mono 之间可以进行转换。对一个 Flux 序列进行计数操作，得到的结果是一个 Mono<Long>对象。
 * 把两个 Mono 序列合并在一起，得到的是一个 Flux 对象。
 * 
 * 创建 Mono
 * 
 * Mono 的创建方式与之前介绍的 Flux 比较相似。Mono 类中也包含了一些与 Flux 类中相同的静态方法。这些方法包括 just()，empty()，error()和 never()等。除了这些方法之外，Mono 还有一些独有的静态方法。

 * 		fromCallable()、fromCompletionStage()、fromFuture()、fromRunnable()和 fromSupplier()：分别从 Callable、CompletionStage、CompletableFuture、Runnable 和 Supplier 中创建 Mono。
 * 		delay(Duration duration)和 delayMillis(long duration)：创建一个 Mono 序列，在指定的延迟时间之后，产生数字 0 作为唯一值。
 * 		ignoreElements(Publisher<T> source)：创建一个 Mono 序列，忽略作为源的 Publisher 中的所有元素，只产生结束消息。
 * 		justOrEmpty(Optional<? extends T> data)和 justOrEmpty(T data)：从一个 Optional 对象或可能为 null 的对象中创建 Mono。只有 Optional 对象中包含值或对象不为 null 时，Mono 序列才产生对应的元素。
 * 
 * @author 	pengpeng
 * @date	2019年10月12日 上午9:26:39
 */
public class MonoExample {

	public static void just() {
		Mono.just(new Date()).subscribe(System.out::println);
	}
	
	public static void fromRunnable() {
		Mono.fromRunnable(() -> {
			System.out.println("running");
		}).subscribe(System.out::println);
	}
	
	public static void fromCallable() {
		Mono.fromCallable(() -> {
			System.out.println("calling");
			return "called";
		}).subscribe(System.out::println);
	}
	
	public static void fromFuture() {
		Mono.fromFuture(CompletableFuture.completedFuture("completed")).subscribe(System.out::println);
	}
	
	public static void fromSupplier() {
		Mono.fromSupplier(() -> {
			System.out.println("suppling");
			return "supplied";
		}).subscribe(System.out::println);
	}
	
	/**
	 * 创建一个 Mono 序列，在指定的延迟时间之后，产生数字 0 作为唯一值。
	 */
	public static void delay() {
		Thread thread = Thread.currentThread();
		Mono.delay(Duration.ofSeconds(10)).doFinally((type) -> {
			System.out.println("type : " + type);
			LockSupport.unpark(thread);
		}).subscribe(System.out::println);
		System.out.println("main");
		LockSupport.park();
	}
	
	/**
	 * 通过 create()方法来使用 MonoSink 来创建 Mono
	 */
	public static void create() {
		Mono.create((sink) -> {
			sink.success("Hello");
		}).subscribe(System.out::println);
	}
	
	/**
	 * 分页查询
	 */
	public static void pagingQuery() {
		int currentPage = 3;
		int pageSize = 20;
		long start = System.currentTimeMillis();
		//压缩getPagingQueryTotal()和getPagingQueryList()两个Mono为一个，同时指定他们两个为并行模式(因为获取总记录数和获取结果集并无关联，可以并行执行以提高性能)
		Mono.zip(getPagingQueryTotal().subscribeOn(Schedulers.parallel()), getPagingQueryList(currentPage, pageSize).subscribeOn(Schedulers.parallel()).collectList(), (totalCount, dataList) -> {
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("code", 200);
			result.put("message", "OK");
			result.put("totalCount", totalCount);
			result.put("data", dataList);
			return result;
		}).subscribe(result -> {
			long end = System.currentTimeMillis();
			System.out.println(result);
			System.out.println(">>> time cost: " + (end - start));
		});
		LockSupport.park();
	}
	
	protected static Flux<Map<String,Object>> getPagingQueryList(int currentPage, int pageSize) {
		return Flux.create(sink -> {
			Map<String,Object> item = null;
			for(int i = 0; i < pageSize; i++) {
				item = new LinkedHashMap<String,Object>();
				int id = (currentPage - 1) * pageSize + i;
				item.put("id", id);
				item.put("name", "user-" + id);
				item.put("time", System.currentTimeMillis());
				sink.next(item);
				System.out.println(String.format("【2】>>> fetch item %s : %s", id, item));
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
			}
			sink.complete();
		});
	}
	
	protected static Mono<Integer> getPagingQueryTotal() {
		return Mono.fromSupplier(() -> {
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
			System.out.println("【1】>>> get total count done!");
			return 1223;
		});
	}
	
	public static void doOnNext() {
		Map<String,Object> context = new HashMap<String,Object>();
		context.put("110", "公安");
		
		boolean switch1 = true;
		boolean switch2 = true;
		
		Mono.just(context).filter(ctx -> switch1).flatMap(ctx -> {
			ctx.put("119", "火警");
			return Mono.just(ctx);
		}).switchIfEmpty(Mono.defer(() -> {
			return Mono.just(context);
		})).filter(ctx -> switch2).flatMap(ctx -> {
			ctx.put("120", "急救");
			return Mono.just(ctx);
		}).switchIfEmpty(Mono.defer(() -> {
			return Mono.just(context);
		})).doOnNext(ctx -> {
			System.out.println("【log】>>> ctx = " + ctx);
		}).subscribe(System.out::println);
	}
	
	public static void main(String[] args) {
		//just();
		//fromRunnable();
		//fromCallable();
		//fromFuture();
		//fromSupplier();
		//delay();
		//create();
		//pagingQuery();
		doOnNext();
	}

}
