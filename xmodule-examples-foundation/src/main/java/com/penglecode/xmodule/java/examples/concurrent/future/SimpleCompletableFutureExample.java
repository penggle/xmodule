package com.penglecode.xmodule.java.examples.concurrent.future;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 简单的CompletableFuture例子
 * 
 * @author 	pengpeng
 * @date	2018年7月13日 下午1:36:14
 */
public class SimpleCompletableFutureExample {

	/**
	 * 1、 创建一个完成的CompletableFuture
	 * 
	 * 最简单的例子就是使用一个预定义的结果创建一个完成的CompletableFuture,通常我们会在计算的开始阶段使用它。
	 */
	public static void completedFuture() {
		CompletableFuture<String> future = CompletableFuture.completedFuture("message"); //定义一个已经完成了的future
		System.out.println(future.isDone()); //future是否已经完成
	    System.out.println(future.getNow(null)); //在future完成的情况下会返回结果(其中有可能抛出异常)，否则返回传入的参数null
	}
	
	/**
	 * 2、运行一个简单的异步阶段
	 * 
	 * CompletableFuture的方法如果以Async结尾，它会异步的执行(没有指定executor的情况下)， 异步执行通过ForkJoinPool实现， 它使用守护线程去执行任务。
	 * 注意这是CompletableFuture的特性， 其它CompletionStage可以override这个默认的行为。
	 */
	public static void runAsync1() {
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			System.out.println("【1】>>> isDaemon : " + Thread.currentThread().isDaemon());
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10)); //使当前等待线程10秒
			System.out.println("Task Completed!");
		});
		System.out.println("【1】>>> isDone : " + future.isDone());
		System.out.println("【1】>>> getNow : " + future.getNow(null));
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(15));
		System.out.println("【2】>>> isDone : " + future.isDone());
		System.out.println("【2】>>> getNow : " + future.getNow(null));
	}
	
	/**
	 * 2、运行一个简单的异步阶段
	 * 
	 * 指定executor
	 */
	public static void runAsync2() {
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			System.out.println("【1】>>> isDaemon : " + Thread.currentThread().isDaemon());
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10)); //使当前等待线程10秒
			System.out.println("Task Completed!");
		}, Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 8));
		System.out.println("【1】>>> isDone : " + future.isDone());
		System.out.println("【1】>>> getNow : " + future.getNow(null));
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(15));
		System.out.println("【2】>>> isDone : " + future.isDone());
		System.out.println("【2】>>> getNow : " + future.getNow(null));
	}
	
	/**
	 * 3、在前一个阶段上应用同步函数
	 * 在前一个阶段完成之后应用一个函数,并且这个被应用的函数的执行是阻塞模式的
	 */
	public static void thenApply() {
		CompletableFuture<String> future = CompletableFuture.completedFuture("java").thenApply((v) -> {
			System.out.println("【1】>>> isDaemon : " + Thread.currentThread().isDaemon());
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10)); //使当前等待线程10秒
			return v.toUpperCase();
		});
		System.out.println("【1】>>> isDone : " + future.isDone());
		System.out.println("【1】>>> getNow : " + future.getNow(null));
	}
	
	/**
	 * 4、在前一个阶段上应用异步函数
	 * 在前一个阶段完成之后应用一个函数,并且这个被应用的函数的执行是非阻塞模式的
	 */
	public static void thenApplyAsync() {
		CompletableFuture<String> future = CompletableFuture.completedFuture("java").thenApplyAsync((v) -> {
			System.out.println("【1】>>> isDaemon : " + Thread.currentThread().isDaemon());
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10)); //使当前等待线程10秒
			return v.toUpperCase();
		});
		System.out.println("【1】>>> isDone : " + future.isDone());
		System.out.println("【1】>>> getNow : " + future.getNow(null));
		System.out.println("【2】>>> getNow : " + future.join()); //等待future完成并返回结果
	}
	
	/**
	 * 5、同步消费前一阶段的结果
	 * 
	 * 如果下一阶段接收了当前阶段的结果，但是在计算的时候不需要返回值(它的返回类型是void)，
	 * 那么它可以不应用一个函数，而是一个消费者， 调用方法也变成了thenAccept:
	 */
	public static void thenAccept() {
		StringBuilder sb = new StringBuilder("hello");
		CompletableFuture.completedFuture("world").thenAccept(v -> {
			sb.append(" " + v);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("12345112");
		});
		System.out.println(sb);
	}
	
	/**
	 * 5、同步消费前一阶段的结果
	 * 
	 * 如果下一阶段接收了当前阶段的结果，但是在计算的时候不需要返回值(它的返回类型是void)，
	 * 那么它可以不应用一个函数，而是一个消费者， 调用方法也变成了thenAccept:
	 */
	public static void thenAccept1() {
		CompletableFuture.supplyAsync(() -> {
			StringBuilder v = new StringBuilder("hello");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("state1");
			return v;
		}).thenAccept(v -> {
			v.append(" world");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("state2");
		});
		System.out.println("end");
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));
	}
	
	/**
	 * 6、异步地消费前一阶段的结果
	 * 
	 * 如果下一阶段接收了当前阶段的结果，但是在计算的时候不需要返回值(它的返回类型是void)，
	 * 那么它可以不应用一个函数，而是一个消费者， 调用方法也变成了thenAccept:
	 */
	public static void thenAcceptAsync() {
		StringBuilder sb = new StringBuilder("hello");
		CompletableFuture.completedFuture("world").thenAcceptAsync(v -> sb.append(" " + v)).join();
		System.out.println(sb);
	}
	
	/**
	 * 7、完成计算异常
	 * 
	 * 现在我们来看一下异步操作如何显式地返回异常，用来指示计算失败。
	 */
	public static void completeExceptionally() {
		CompletableFuture<String> future = CompletableFuture.completedFuture("java").thenApplyAsync((v) -> {
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10)); //使当前等待线程10秒
			return v.toUpperCase();
		});
		future.completeExceptionally(new RuntimeException("我是故意的^_^"));
		try {
			future.join();
		} catch (Exception e) {
			System.out.println(e.getClass());
			System.out.println(e.getCause().getClass());
			System.out.println(e.getMessage());
			System.out.println(e.getCause().getMessage());
		}
	}
	
	/**
	 * 8、阶段内发生异常的处理方式一
	 */
	public static void exceptionally() {
		CompletableFuture<Void> future = CompletableFuture.completedFuture("java.util.Date1").thenAcceptAsync((v) -> {
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5)); //使当前等待线程5秒
			try {
				Class.forName(v);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("ClassNotFoundException : " + e.getMessage());
			}
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5)); //使当前等待线程5秒
		}).exceptionally((e) -> {
			System.out.println(e.getClass());
			System.out.println(e.getCause().getClass());
			System.out.println(e.getMessage());
			System.out.println(e.getCause().getMessage());
			return null;
		});
		System.out.println("【1】>>> isDone : " + future.isDone());
		future.join();
		System.out.println("【2】>>> isDone : " + future.isDone());
	}
	
	/**
	 * 9、阶段内发生异常的处理方式二
	 */
	public static void handle() {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			try {
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5)); //使当前等待线程5秒
				return Files.lines(Paths.get("/etc/localtime"), Charset.forName("UTF-8")).collect(Collectors.joining("\\n"));
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}).handle((v, e) -> {
			System.out.println(e.getClass());
			System.out.println(e.getCause().getClass());
			System.out.println(e.getMessage());
			System.out.println(e.getCause().getMessage());
			return "Asia/Shanghai";
		});
		System.out.println("【1】>>> isDone : " + future.isDone());
		System.out.println("【1】>>> getNow : " + future.getNow(null));
		future.join();
		System.out.println("【2】>>> isDone : " + future.isDone());
		System.out.println("【2】>>> getNow : " + future.getNow(null));
	}
	
	/**
	 * 10、当计算完成时的后置处理
	 */
	public static void whenComplete() {
		CompletableFuture.supplyAsync(() -> {
			try {
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5)); //使当前等待线程5秒
				return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?user=root&password=123456");
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}).thenApplyAsync(conn -> {
			try {
				ResultSet rs = conn.getMetaData().getTables(null, null, null, new String[] {"TABLE"});
				while(rs.next()) {
					String tableName = rs.getString(3).toLowerCase();
					System.out.println(tableName);
				}
				return conn;
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}).whenComplete((conn, e) -> {
			try {
				conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			if(e != null) {
				System.out.println(e.getClass());
				System.out.println(e.getCause().getClass());
				System.out.println(e.getMessage());
				System.out.println(e.getCause().getMessage());
			}
		});
		
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(15));
	}
	
	/**
	 * 11、取消计算
	 * 和完成异常类似，我们可以调用cancel(boolean mayInterruptIfRunning)取消计算。
	 * 对于CompletableFuture类，布尔参数并没有被使用，这是因为它并没有使用中断去取消操作，
	 * 相反，cancel等价于completeExceptionally(new CancellationException())。
	 * 
	 */
	public static void cancel() {
		CompletableFuture<Void> future = CompletableFuture.completedFuture("java").thenAcceptAsync((v) -> {
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10)); //使当前等待线程5秒
		});
		CompletableFuture<Void> exception = future.exceptionally((e) -> {
			System.out.println(e.getClass());
			System.out.println(e.getMessage());
			return null;
		});
		System.out.println("【1】>>> isDone : " + future.isDone());
		future.cancel(true); //触发CancellationException异常
		System.out.println("【2】>>> isDone : " + future.isDone());
		System.out.println("【2】>>> isCompletedExceptionally : " + future.isCompletedExceptionally());
		exception.join();
	}
	
	/**
	 * 12、在两个完成的阶段其中之一上应用函数
	 * 本例，模拟两个发送消息的阶段，只要他们之一把消息发送出去了，则会触发eitherSendDone函数的执行
	 */
	public static void applyToEither() {
		Random random = new Random();
		CompletableFuture<String> messageFuture = CompletableFuture.completedFuture("message");
		
		Function<String,String> eitherSendDone = m -> {
			System.out.println(">>> this message was sent out at least once!");
			return "OK";
		};
		
		CompletableFuture<String> sendFuture2 = messageFuture.thenApplyAsync(m -> {
			int seconds = random.nextInt(10);
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds)); //随机秒数
			System.out.println(String.format("【2】>>> send message takes %s seconds", seconds));
			return m;
		});
		
		CompletableFuture<String> sendFuture1 = messageFuture.thenApplyAsync(m -> {
			int seconds = random.nextInt(10);
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds)); //随机秒数
			System.out.println(String.format("【1】>>> send message takes %s seconds", seconds));
			return m;
		}).applyToEither(sendFuture2, eitherSendDone);
		
		System.out.println(">>> send done : " + sendFuture1.join());
	}
	
	/**
	 * 13、在两个完成的阶段其中之一上应用函数
	 * 和前一个例子很类似了，只不过我们调用的是消费者函数 (Function变成Consumer):
	 */
	public static void acceptEither() {
		Random random = new Random();
		CompletableFuture<String> messageFuture = CompletableFuture.completedFuture("message");
		
		Consumer<String> eitherSendDone = m -> {
			System.out.println(">>> this message was sent out at least once!");
		};
		
		CompletableFuture<String> sendFuture2 = messageFuture.thenApplyAsync(m -> {
			int seconds = random.nextInt(10);
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds)); //随机秒数
			System.out.println(String.format("【2】>>> send message takes %s seconds", seconds));
			return m;
		});
		
		CompletableFuture<Void> sendFuture1 = messageFuture.thenApplyAsync(m -> {
			int seconds = random.nextInt(10);
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds)); //随机秒数
			System.out.println(String.format("【1】>>> send message takes %s seconds", seconds));
			return m;
		}).acceptEither(sendFuture2, eitherSendDone);
		
		System.out.println(">>> send done : " + sendFuture1.join());
	}
	
	/**
	 * 14、在两个阶段都执行完后运行一个 Runnable
	 * 本例，模拟两个发送消息的阶段，必须他们都把消息发送出去了，才会触发bothSendDone的执行
	 */
	public static void runAfterBoth() {
		Random random = new Random();
		CompletableFuture<String> messageFuture = CompletableFuture.completedFuture("message");
		
		Runnable bothSendDone = () -> {
			System.out.println(">>> this message was sent out both!");
		};
		
		CompletableFuture<String> sendFuture2 = messageFuture.thenApplyAsync(m -> {
			int seconds = random.nextInt(10);
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds)); //随机秒数
			System.out.println(String.format("【2】>>> send message takes %s seconds", seconds));
			return m;
		});
		
		CompletableFuture<Void> sendFuture1 = messageFuture.thenApplyAsync(m -> {
			int seconds = random.nextInt(10);
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds)); //随机秒数
			System.out.println(String.format("【1】>>> send message takes %s seconds", seconds));
			return m;
		}).runAfterBoth(sendFuture2, bothSendDone);
		
		System.out.println(">>> sent done : " + sendFuture1.join());
	}
	
	/**
	 * 15、使用BiConsumer处理两个阶段的结果
	 */
	public static void thenAcceptBoth() {
		Random random = new Random();
		CompletableFuture<String> messageFuture = CompletableFuture.completedFuture("message");
		
		BiConsumer<Integer,Integer> bothSendDone = (s1, s2) -> {
			System.out.println(String.format(">>> this message was sent out both and takes %s seconds!", Math.max(s1, s2)));
		};
		
		CompletableFuture<Integer> sendFuture2 = messageFuture.thenApplyAsync(m -> {
			int seconds = random.nextInt(10);
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds)); //随机秒数
			System.out.println(String.format("【2】>>> send message takes %s seconds", seconds));
			return seconds;
		});
		
		CompletableFuture<Void> sendFuture1 = messageFuture.thenApplyAsync(m -> {
			int seconds = random.nextInt(10);
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds)); //随机秒数
			System.out.println(String.format("【1】>>> send message takes %s seconds", seconds));
			return seconds;
		}).thenAcceptBoth(sendFuture2, bothSendDone);
		
		System.out.println(">>> sent done : " + sendFuture1.join());
	}
	
	/**
	 * 15、使用BiFunction处理两个阶段的结果
	 */
	public static void thenCombine() {
		Random random = new Random();
		CompletableFuture<String> messageFuture = CompletableFuture.completedFuture("message");
		
		BiFunction<Integer,Integer,Integer> bothSendDone = (s1, s2) -> {
			int maxSeconds = Math.max(s1, s2);
			System.out.println(String.format(">>> this message was sent out both!", maxSeconds));
			return maxSeconds;
		};
		
		CompletableFuture<Integer> sendFuture2 = messageFuture.thenApplyAsync(m -> {
			int seconds = random.nextInt(10);
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds)); //随机秒数
			System.out.println(String.format("【2】>>> send message takes %s seconds", seconds));
			return seconds;
		});
		
		CompletableFuture<Integer> sendFuture1 = messageFuture.thenApplyAsync(m -> {
			int seconds = random.nextInt(10);
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds)); //随机秒数
			System.out.println(String.format("【1】>>> send message takes %s seconds", seconds));
			return seconds;
		}).thenCombine(sendFuture2, bothSendDone);
		
		System.out.println(">>> sent done, total takes %s seconds : " + sendFuture1.join());
	}
	
	/**
	 * 16、组合CompletableFuture
	 * 被组合的两个阶段的执行是有先后依赖关系的。
	 */
	public static void thenCompose() {
		Random random = new Random();
		String url = "https://www.sojson.com/open/api/weather/json.shtml?city=%s";
		
		CompletableFuture<String> weatherFuture = CompletableFuture.completedFuture("北京");
		
		CompletableFuture<Map<String,Object>> getWeatherFuture = weatherFuture.thenApplyAsync(city -> {
			int seconds = random.nextInt(10);
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds)); //随机秒数
			System.out.println(String.format("【1】>>> get weather info of city(%s) by url : %s, takes %s seconds", city, String.format(url, city), seconds));
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("temp", "36℃");
			data.put("type", "晴");
			data.put("wind", "东南风, 3~4级");
			return data;
		});
		
		CompletableFuture<String> saveWeatherFuture = getWeatherFuture.thenCompose(data -> {
			return weatherFuture.thenApplyAsync(city -> {
				int seconds = random.nextInt(10);
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds)); //随机秒数
				System.out.println(String.format("【2】>>> save weather data of city(%s) : %s, takes %s seconds", city, data, seconds));
				return city;
			});
		});
		
		System.out.println(String.format(">>> get and save weather info of city(%s) done: ", saveWeatherFuture.join()));
	}
	
	/**
	 * 17、当几个阶段中的任意一个完成，创建一个完成的阶段
	 * 有点类似于例12，只不过例12是两个阶段，此例中则有任意个阶段
	 * 此例模拟一场百米竞赛，并角逐出冠军
	 */
	public static void anyOf() {
		Random random = new Random();
		List<String> runners = Arrays.asList("张三", "李四", "王五", "陈六", "赵七");
		
		List<CompletableFuture<Object[]>> runningFutures = runners.stream().map(item -> {
			return CompletableFuture.completedFuture(item).thenApplyAsync(runner -> {
				int seconds = random.nextInt(10);
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds)); //随机秒数
				System.out.println(String.format("【%s】>>> the runner %s has finishied the race, takes %s seconds", Thread.currentThread().getName(), runner, seconds));
				return new Object[] {runner, seconds};
			});
		}).collect(Collectors.toList());
		
		CompletableFuture.anyOf(runningFutures.toArray(new CompletableFuture[0])).thenAccept(champion -> {
			Object[] results = (Object[]) champion;
			System.out.println(String.format("【%s】>>> the runner %s won the race, takes the fastest speed with %s seconds", Thread.currentThread().getName(), results[0], results[1]));
		});
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(15));
	}
	
	/**
	 * 两个都是同步的阶段串联
	 */
	public static void testSyncAndSync() {
		CompletableFuture.completedFuture("message").thenApply(m -> {
			System.out.println("【thenApply】>>> " + Thread.currentThread().getName());
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
			return m.toUpperCase();
		}).thenAccept(m -> {
			System.out.println("【thenAccept】>>> " + Thread.currentThread().getName());
			System.out.println(m);
		});
	}
	
	/**
	 * 前一个同步阶段和后一个异步阶段串联
	 */
	public static void testSyncAndAsync() {
		CompletableFuture.completedFuture("message").thenApply(m -> {
			System.out.println("【thenApply】>>> " + Thread.currentThread().getName());
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
			return m.toUpperCase();
		}).thenAcceptAsync(m -> {
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5)); //加入了等待，检测该异步阶段与主线程的关系，很明显该异步阶段将迟于主线程结束，除非加上join()
			System.out.println("【thenAcceptAsync】>>> " + Thread.currentThread().getName());
			System.out.println(m);
		});
	}
	
	/**
	 * 前一个异步阶段和后一个同步阶段串联
	 */
	public static void testAsyncAndSync1() {
		CompletableFuture.completedFuture("message").thenApplyAsync(m -> {
			System.out.println("【thenApplyAsync】>>> " + Thread.currentThread().getName());
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
			return m.toUpperCase();
		}).thenAccept(m -> {
			System.out.println("【thenAccept】>>> " + Thread.currentThread().getName());
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
			System.out.println(m);
		}).join(); //试试去掉join有什么问题
	}
	
	/**
	 * 前一个异步阶段和后一个同步阶段串联
	 */
	public static void testAsyncAndSync2() {
		CompletableFuture.completedFuture("message").thenApplyAsync(m -> {
			System.out.println("【thenApplyAsync】>>> " + Thread.currentThread().getName());
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
			return m.toUpperCase();
		}).thenAccept(m -> {
			System.out.println("【thenAccept】>>> " + Thread.currentThread().getName());
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
			System.out.println(m);
		});
		
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(15)); //去掉join()，改用主线程等待来看看thenApplyAsync与thenAccept的执行顺序
	}
	
	/**
	 * 前一个异步阶段和后一个同步阶段组合
	 */
	public static void testAsyncAndSync3() {
		CompletableFuture.completedFuture("message").thenApplyAsync(m -> {
			System.out.println("【thenApplyAsync】>>> " + Thread.currentThread().getName());
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
			return m.toUpperCase();
		}).thenCompose(m -> {
			return CompletableFuture.supplyAsync(() -> {
				System.out.println("【thenAccept】>>> " + Thread.currentThread().getName());
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
				return m;
			});
		}).thenAccept(m -> {
			System.out.println(m);
		}).join();
	}
	
	public static void main(String[] args) {
		//completedFuture();
		//runAsync1();
		//runAsync2();
		//thenApply();
		//thenApplyAsync();
		//thenAccept();
		thenAccept1();
		//thenAcceptAsync();
		//completeExceptionally();
		//exceptionally();
		//handle();
		//whenComplete();
		//cancel();
		//applyToEither();
		//acceptEither();
		//runAfterBoth();
		//thenAcceptBoth();
		//thenCombine();
		//thenCompose();
		//anyOf();
		//testSyncAndSync();
		//testSyncAndAsync();
		//testAsyncAndSync1();
		//testAsyncAndSync2();
		//testAsyncAndSync3();
	}

}
