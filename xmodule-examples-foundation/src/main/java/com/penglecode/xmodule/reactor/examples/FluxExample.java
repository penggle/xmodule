package com.penglecode.xmodule.reactor.examples;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.reactivestreams.Subscription;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Flux 表示的是包含 0 到 N 个元素的异步序列。在该序列中可以包含三种不同类型的消息通知：正常的包含元素的消息、序列结束的消息和序列出错的消息。当消息通知产生时，订阅者中对应的方法 onNext(), onComplete()和 onError()会被调用。
 * 
 * 创建 Flux，有多种不同的方式可以创建 Flux 序列。
 * 
 * 第一种方式是通过 Flux 类中的静态方法。
 * 
 * 		just()：可以指定序列中包含的全部元素。创建出来的 Flux 序列在发布这些元素之后会自动结束。
 * 		fromArray()，fromIterable()和 fromStream()：可以从一个数组、Iterable 对象或 Stream 对象中创建 Flux 对象。
 * 		empty()：创建一个不包含任何元素，只发布结束消息的序列。
 * 		error(Throwable error)：创建一个只包含错误消息的序列。
 * 		never()：创建一个不包含任何消息通知的序列。
 * 		range(int start, int count)：创建包含从 start 起始的 count 个数量的 Integer 对象的序列。
 * 		interval(Duration period)和 interval(Duration delay, Duration period)：创建一个包含了从 0 开始递增的 Long 对象的序列。其中包含的元素按照指定的间隔来发布。除了间隔时间之外，还可以指定起始元素发布之前的延迟时间。
 * 		intervalMillis(long period)和 intervalMillis(long delay, long period)：与 interval()方法的作用相同，只不过该方法通过毫秒数来指定时间间隔和延迟时间。
 * 
 * 
 * Reactor中提供了非常丰富的操作符，除了以上几个常见的，还有：
 * 
 * 用于编程方式自定义生成数据流的create和generate等及其变体方法；
 * 用于“无副作用的peek”场景的doOnNext、doOnError、doOncomplete、doOnSubscribe、doOnCancel等及其变体方法；
 * 用于数据流转换的when、and/or、merge、concat、collect、count、repeat等及其变体方法；
 * 用于过滤/拣选的take、first、last、sample、skip、limitRequest等及其变体方法；
 * 用于错误处理的timeout、onErrorReturn、onErrorResume、doFinally、retryWhen等及其变体方法；
 * 用于分批的window、buffer、group等及其变体方法；
 * 用于线程调度的publishOn和subscribeOn方法。
 * 
 * 
 * @see https://blog.csdn.net/get_set/article/details/79480172
 * @author 	pengpeng
 * @date	2019年10月10日 下午4:52:10
 */
public class FluxExample {

	/**
	 * 创建Flux
	 * 
	 * 可以指定序列中包含的全部元素。创建出来的 Flux 序列在发布这些元素之后会自动结束。
	 */
	public static void just() {
		Flux.just("java", "c++", "go", "ruby", "python", "nodejs").sort().subscribe(System.out::println);
	}
	
	/**
	 * 创建Flux
	 * 
	 * 可以从一个数组、Iterable 对象或 Stream 对象中创建 Flux 对象。
	 */
	public static void fromArray() {
		Flux.fromArray(new Integer[] {1,2,3}).sort(Comparator.reverseOrder()).subscribe(System.out::println);
	}
	
	/**
	 * 创建Flux
	 * 
	 * 创建包含从 start 起始的 count 个数量的 Integer 对象的序列。
	 */
	public static void range() {
		Flux.range(1, 10).subscribe(System.out::println);
	}
	
	/**
	 * 创建Flux
	 * 
	 * 创建一个包含了从 0 开始递增的 Long 对象的序列。其中包含的元素按照指定的间隔来发布。
	 * 除了间隔时间之外，还可以指定起始元素发布之前的延迟时间。
	 */
	public static void interval1() {
		Thread thread = Thread.currentThread();
		Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).doOnComplete(() -> {
			//doOnComplete代码永远不会进入
			LockSupport.unpark(thread); //Unreachable code
		}).subscribe(System.out::println);
		LockSupport.park();
	}
	
	/**
	 * 创建Flux
	 * 
	 * generate()方法通过同步和逐一的方式来产生 Flux 序列
	 */
	public static void generate1() {
		Flux.generate(sink -> {
			sink.next("Hello");
			sink.complete();
		}).subscribe(System.out::println);
	}
	
	/**
	 * 创建Flux
	 * 
	 * generate()方法通过同步和逐一的方式来产生 Flux 序列
	 */
	public static void generate2() {
		final Random random = new Random();
		Flux.generate(() -> 10, (s, sink) -> {
			sink.next(random.nextInt());
			if(--s <= 0) {
				sink.complete();
			}
			return s;
		}).subscribe(System.out::println);
	}
	
	/**
	 * 创建Flux
	 * 
	 * create()方法与 generate()方法的不同之处在于所使用的是 FluxSink 对象。
	 * FluxSink 支持同步和异步的消息产生，并且可以在一次调用中产生多个元素。
	 */
	public static void create1() {
		Flux.create(sink -> {
			for (int i = 0; i < 10; i++) {
		        sink.next(i);
		    }
		    sink.complete();
		}).subscribe(System.out::println);
	}
	
	/**
	 * buffer - 把当前流中的元素收集到集合中，并把集合对象作为流中的新元素
	 * 
	 * 输出的是 5 个包含 20 个元素的数组
	 */
	public static void buffer1() {
		Flux.range(1, 100).buffer(20).subscribe(System.out::println);
	}
	
	/**
	 * buffer - 把当前流中的元素收集到集合中，并把集合对象作为流中的新元素
	 * 
	 * 输出的是 2 个包含了 10 个元素的数组
	 */
	public static void buffer2() {
		/**
		 * interval方法中从0开始递增式每1秒生成一个元素，生成FluxA
		 * buffer方法中每10秒多点收集一次，生成由10个元素组成的新流FluxB
		 * take方法在FluxB流基础上取前2个生成一个新流FluxC
		 * toStream方法把 FluxC 序列转换成 Java 8 中的 Stream 对象，再通过 forEach()方法来进行输出。这是因为序列的生成是异步的，而转换成 Stream 对象可以保证主线程在序列生成完成之前不会退出
		 */
		Flux.interval(Duration.ofMillis(1000)).buffer(Duration.ofMillis(10001)).take(2).toStream().forEach(System.out::println);;
	}
	
	/**
	 * buffer - 把当前流中的元素收集到集合中，并把集合对象作为流中的新元素
	 * 
	 * 输出的是 5 个包含 2 个元素的数组，每当遇到一个偶数就会结束当前的收集
	 */
	public static void bufferUntil() {
		Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);
	}
	
	/**
	 * buffer - 把当前流中的元素收集到集合中，并把集合对象作为流中的新元素
	 * 
	 * 输出的是 5 个包含 1 个元素的数组，数组里面包含的只有偶数。
	 */
	public static void bufferWhile() {
		Flux.range(1, 10).bufferWhile(i -> i % 2 == 0).subscribe(System.out::println);
	}
	
	/**
	 * filter - 对流中包含的元素进行过滤，只留下满足 Predicate 指定条件的元素。
	 */
	public static void filter() {
		Flux.range(1, 10).filter(i -> i % 2 == 0).subscribe(System.out::println);
	}
	
	/**
	 * window 操作符的作用类似于 buffer，所不同的是 window 操作符是把当前流中的元素收集到另外的 Flux 序列中，因此返回值类型是 Flux<Flux<T>>
	 */
	public static void window() {
		Flux.range(1, 100).window(20).subscribe(flux -> {
			flux.subscribe(System.out::println);
			System.out.println("----------------------");
		});
	}
	
	/**
	 * zipWith 操作符把当前流中的元素与另外一个流中的元素按照一对一的方式进行合并。
	 * 在合并时可以不做任何处理，由此得到的是一个元素类型为 Tuple2 的流；
	 * 也可以通过一个 BiFunction 函数对合并的元素进行处理，所得到的流的元素类型为该函数的返回值。
	 */
	public static void zipWith() {
		Flux.just("a", "b", "c").zipWith(Flux.just("x", "y", "z")).subscribe(System.out::println);
		
		System.out.println("----------------------------------");
		
		Flux.just("a", "b").zipWith(Flux.just("c", "d"), (s1, s2) -> String.format("%s-%s", s1, s2)).subscribe(System.out::println);
	}
	
	/**
	 * take 系列操作符用来从当前流中提取元素。提取的方式可以有很多种。
	 * 
	 * take(long n)，take(Duration timespan)和 takeMillis(long timespan)：按照指定的数量或时间间隔来提取。
	 * takeLast(long n)：提取流中的最后 N 个元素。
	 * takeUntil(Predicate<? super T> predicate)：提取元素直到 Predicate 返回 true。
	 * takeWhile(Predicate<? super T> continuePredicate)： 当 Predicate 返回 true 时才进行提取。
	 * takeUntilOther(Publisher<?> other)：提取元素直到另外一个流开始产生元素。
	 */
	public static void take() {
		Flux.range(1, 1000).take(10).subscribe(System.out::println);
		System.out.println("-----------------------------------------------------");
		Flux.range(1, 1000).takeLast(10).subscribe(System.out::println);
		System.out.println("-----------------------------------------------------");
		Flux.range(1, 1000).takeWhile(i -> i < 10).subscribe(System.out::println);
		System.out.println("-----------------------------------------------------");
		Flux.range(1, 1000).takeUntil(i -> i == 10).subscribe(System.out::println);
	}
	
	/**
	 * reduce 和 reduceWith 操作符对流中包含的所有元素进行累积操作，得到一个包含计算结果的 Mono 序列。累积操作是通过一个 BiFunction 来表示的。在操作时可以指定一个初始值。如果没有初始值，则序列的第一个元素作为初始值。
	 */
	public static void reduce() {
		//1到100的求和运算
		Flux.range(1, 100).reduce((x, y) -> x + y).subscribe(System.out::println);
		//提供一个初始值100
		Flux.range(1, 100).reduceWith(() -> 100, (x, y) -> x + y).subscribe(System.out::println);
	}
	
	/**
	 * merge 和 mergeSequential
	 * merge 和 mergeSequential 操作符用来把多个流合并成一个 Flux 序列。
	 * 不同之处在于 merge 按照所有流中元素的实际产生顺序来合并，
	 * 而 mergeSequential 则按照所有流被订阅的顺序，以流为单位进行合并。
	 */
	public static void merge() {
		Flux.merge(Flux.interval(Duration.ofMillis(0), Duration.ofMillis(100)).take(5),
				Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5)).toStream()
				.forEach(System.out::println);
		System.out.println("-----------------------------------------------------");
		Flux.mergeSequential(Flux.interval(Duration.ofMillis(0), Duration.ofMillis(100)).take(5),
				Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5)).toStream()
				.forEach(System.out::println);
	}
	
	/**
	 * 消息处理
	 * 
	 * 当需要处理 Flux 或 Mono 中的消息时，如之前的代码清单所示，
	 * 可以通过 subscribe 方法来添加相应的订阅逻辑。在调用 subscribe 方法时可以指定需要处理的消息类型。
	 * 可以只处理其中包含的正常消息，也可以同时处理错误消息和完成消息。
	 * 以下代码中通过 subscribe()方法同时处理了正常消息和错误消息。
	 */
	public static void subscribe1() {
		Flux.just(1, 2)
        	.concatWith(Mono.error(new IllegalStateException()))
        	.subscribe(System.out::println, System.err::println);
	}
	
	/**
	 * 消息处理
	 * 
	 * 正常的消息处理相对简单。当出现错误时，有多种不同的处理策略。
	 * 第一种策略是通过 onErrorReturn()方法返回一个默认值。如下列代码所示：
	 */
	public static void subscribe2() {
		Flux.just(1, 2)
        	.concatWith(Mono.error(new IllegalStateException()))
        	.onErrorReturn(0) //当出现错误时，流会产生默认值 0
        	.subscribe(System.out::println);
	}
	
	/**
	 * 消息处理
	 * 
	 * 第二种策略是通过 onErrorResumeWith()方法来根据不同的异常类型来选择要使用的产生元素的流。如下列代码所示：
	 */
	public static void subscribe3() {
		Flux.just(1, 2)
        	.concatWith(Mono.error(new IllegalStateException()))
        	.onErrorResume(e -> {
        		if(e instanceof IllegalArgumentException) {
        			return Mono.just(0);
        		} else if (e instanceof IllegalStateException) {
        			return Mono.just(-1);
        		}
        		return Mono.empty();
        	}) //将产生 Mono.just(0)对应的流，也就是数字 0
        	.concatWith(Mono.just(4))
        	.subscribe(System.out::println);
	}
	
	/**
	 * 消息处理
	 * 
	 * 第三种策略还可以通过 retry 操作符来进行重试。如下列代码所示：
	 */
	public static void subscribe4() {
		Flux.just(1, 2)
        	.concatWith(Mono.error(new IllegalStateException()))
        	.retry(1)
        	.subscribe(System.out::println);
	}
	
	/**
	 * 消息处理
	 */
	public static void subscribe5() {
		Flux.range(1, 6)
			.map(i -> 10 / (i - 3))
			.onErrorReturn(0) // 1
			.map(i -> i * i)
			.subscribe(System.out::println, System.err::println);
	}
	
	/**
	 * 通过调度器（Scheduler）可以指定这些操作执行的方式和所在的线程。有下面几种不同的调度器实现。

	 * 		当前线程，通过 Schedulers.immediate()方法来创建。
	 * 		单一的可复用的线程，通过 Schedulers.single()方法来创建。
	 * 		使用弹性的线程池，通过 Schedulers.elastic()方法来创建。线程池中的线程是可以复用的。当所需要时，新的线程会被创建。如果一个线程闲置太长时间，则会被销毁。该调度器适用于 I/O 操作相关的流的处理。
	 * 		使用对并行操作优化的线程池，通过 Schedulers.parallel()方法来创建。其中的线程数量取决于 CPU 的核的数量。该调度器适用于计算密集型的流的处理。
	 * 		使用支持任务调度的调度器，通过 Schedulers.timer()方法来创建。
	 * 		从已有的 ExecutorService 对象中创建调度器，通过 Schedulers.fromExecutorService()方法来创建。
	 * 		某些操作符默认就已经使用了特定类型的调度器。比如 intervalMillis()方法创建的流就使用了由 Schedulers.timer()创建的调度器。通过 publishOn()和 subscribeOn()方法可以切换执行操作的调度器。其中 publishOn()方法切换的是操作符的执行方式，而 subscribeOn()方法切换的是产生流中元素时的执行方式。
	 * 
	 * 如下代码中，使用 create()方法创建一个新的 Flux 对象，其中包含唯一的元素是当前线程的名称。
	 * 接着是两对 publishOn()和 map()方法，其作用是先切换执行时的调度器，再把当前的线程名称作为前缀添加。
	 * 最后通过 subscribeOn()方法来改变流产生时的执行方式。
	 * 运行之后的结果是[elastic-2] [single-1] parallel-1。
	 * 最内层的线程名字 parallel-1 来自产生流中元素时使用的 Schedulers.parallel()调度器，中间的线程名称 single-1 来自第一个 map 操作之前的 Schedulers.single()调度器，最外层的线程名字 elastic-2 来自第二个 map 操作之前的 Schedulers.elastic()调度器。
	 * 
	 */
	public static void scheduler() {
		Flux.create(sink -> {
		    sink.next(Thread.currentThread().getName());
		    sink.complete();
		})
		.publishOn(Schedulers.single())
		.map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
		.publishOn(Schedulers.elastic())
		.map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
		.subscribeOn(Schedulers.parallel())
		.toStream()
		.forEach(System.out::println);
	}
	
	/**
	 * 使用检查点
	 * 
	 * 另外一种做法是通过 checkpoint 操作符来对特定的流处理链来启用调试模式。
	 * 以下代码中，在 map 操作符之后添加了一个名为 test 的检查点。当出现错误时，检查点名称会出现在异常堆栈信息中。
	 * 对于程序中重要或者复杂的流处理链，可以在关键的位置上启用检查点来帮助定位可能存在的问题。
	 */
	public static void debug() {
		Flux.just(1, 0).map(x -> 1 / x).checkpoint("test").subscribe(System.out::println);
	}
	
	/**
	 * 日志记录
	 * 
	 * 在开发和调试中的另外一项实用功能是把流相关的事件记录在日志中。这可以通过添加 log 操作符来实现。
	 * 以下代码中，添加了 log 操作符并指定了日志分类的名称。
	 */
	public static void log() {
		Flux.range(1, 2).log("Range").subscribe(System.out::println);
	}
	
	/**
	 * flatMap 操作符把流中的每个元素转换成一个流，再把所有流中的元素进行合并
	 */
	public static void flatMap() {
		Flux.just(5, 10)
        	.flatMap(i -> Flux.range(1, i))
        	.subscribe(System.out::println);
	}
	
	/**
	 * 回压
	 * 
	 * 消费速度控制
	 */
	public static void backpressure1() {
		Flux.range(1, 6)
			.doOnRequest(n -> System.out.println("Request " + n + " values...")) //在每次request的时候打印request个数
			.subscribe(new BaseSubscriber<Integer>() { //自定义Subscriber,用于在特殊场景下控制backpressure

				/**
				 * 定义在订阅的时候执行的操作
				 * (该方法仅执行一次)
				 */
				@Override
				protected void hookOnSubscribe(Subscription subscription) {
					System.out.println("Subscribed and make a request...");
                    request(1); //订阅时首先向上游请求1个元素
				}

				/**
				 * 定义每次在收到一个元素的时候的操作
				 */
				@Override
				protected void hookOnNext(Integer value) {
					LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
                    System.out.println("Get value [" + value + "]");
                    request(1); //每次处理完1个元素后再请求1个
				}
			});
	}
	
	/**
	 * 回压
	 * 
	 * 消费速度控制
	 */
	public static void backpressure2() {
		Flux.range(1, 105)
			.doOnRequest(n -> System.out.println("Request " + n + " values...")) //在每次request的时候打印request个数
			.subscribe(new BaseSubscriber<Integer>() { //自定义Subscriber,用于在特殊场景下控制backpressure

				private final int batchs = 10;
				
				private final List<Integer> tempList = new ArrayList<Integer>();
				
				/**
				 * 定义在订阅的时候执行的操作
				 * (该方法仅执行一次)
				 */
				@Override
				protected void hookOnSubscribe(Subscription subscription) {
					System.out.println("Subscribed and make a request...");
                    request(batchs); //订阅时首先向上游请求batchs个元素
				}

				/**
				 * 定义每次在收到一个元素的时候的操作
				 */
				@Override
				protected void hookOnNext(Integer value) {
					tempList.add(value);
					if(tempList.size() == batchs) {
						LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
	                    System.out.println("Handling values: " + tempList);
	                    tempList.clear();
	                    request(10); //每次处理完1个元素后再请求1个
					}
				}
			});
	}
	
	public static void main(String[] args) {
		//just();
		//fromArray();
		//range();
		//interval1();
		//generate1();
		//generate2();
		//create1();
		//buffer1();
		//buffer2();
		//bufferUntil();
		//bufferWhile();
		//filter();
		//window();
		//zipWith();
		//take();
		//reduce();
		//merge();
		//subscribe1();
		//subscribe2();
		//subscribe3();
		//subscribe4();
		//subscribe5();
		//scheduler();
		//debug();
		//log();
		//flatMap();
		//backpressure1();
		backpressure2();
	}

}
