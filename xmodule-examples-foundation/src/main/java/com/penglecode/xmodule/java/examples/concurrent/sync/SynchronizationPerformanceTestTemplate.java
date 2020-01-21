package com.penglecode.xmodule.java.examples.concurrent.sync;

import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizationPerformanceTestTemplate {

	public ExecutorService createExecutorService() {
		return Executors.newCachedThreadPool();
	}
	
	public long doTest(Sequence sequence, int rounds, int tasks) throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		CountDownLatch countDownLatch = new CountDownLatch(tasks);
		Runnable command = new SynchronizationPerformanceCommand(sequence, countDownLatch, rounds);
		ExecutorService executorService = createExecutorService();
		for(int i = 0; i < tasks; i++){
			executorService.execute(command);
		}
		executorService.shutdown();
		countDownLatch.await();
		long endTimeMillis = System.currentTimeMillis();
		long timeMillis = endTimeMillis - beginTimeMillis;
		/**
		 * get next sequence value concurrently from 0 to 1,000,000 by sequence[SynchronizedSequence], cost 68 milliseconds [平均68秒]
		 * 
		 */
		System.out.println(MessageFormat.format("get next sequence value concurrently from 0 to {0} by sequence[{1}], cost {2} milliseconds", sequence.getCurrSequenceValue(), sequence.getClass().getSimpleName(), timeMillis));
		return timeMillis;
	}
	
	/**
	 * AVG cost 4742.0 milliseconds, rounds = 10000, tasks = 10000, sequence = SynchronizedSequence
	 * AVG cost 5122.0 milliseconds, rounds = 100000, tasks = 1000, sequence = SynchronizedSequence
	 * AVG cost 5006.9 milliseconds, rounds = 200000, tasks = 500, sequence = SynchronizedSequence
	 * 
	 * AVG cost 90.2 milliseconds, rounds = 100, tasks = 10000, sequence = SynchronizedSequence
	 * AVG cost 59.7 milliseconds, rounds = 1000, tasks = 1000, sequence = SynchronizedSequence
	 * AVG cost 50.3 milliseconds, rounds = 10000, tasks = 100, sequence = SynchronizedSequence
	 * AVG cost 50.2 milliseconds, rounds = 20000, tasks = 50, sequence = SynchronizedSequence
	 * AVG cost 50.3 milliseconds, rounds = 100000, tasks = 10, sequence = SynchronizedSequence
	 */
	public void test4Synchronized(int rounds, int tasks) throws Exception {
		long totalTimeMillis = 0;
		int count = 10;
		for(int i = 0; i < count; i++){
			totalTimeMillis += doTest(new SynchronizedSequence(), rounds, tasks);
		}
		System.out.println("AVG cost " + (totalTimeMillis * 1.0 / count) + " milliseconds, rounds = " + rounds + ", tasks = " + tasks + ", sequence = SynchronizedSequence");
	}
	
	/**
	 * AVG cost 3327.6 milliseconds, rounds = 10000, tasks = 10000, sequence = LockSequence
	 * AVG cost 3067.5 milliseconds, rounds = 100000, tasks = 1000, sequence = LockSequence
	 * AVG cost 3103.9 milliseconds, rounds = 200000, tasks = 500, sequence = LockSequence
	 * 
	 * 
	 * AVG cost 86.1 milliseconds, rounds = 100, tasks = 10000, sequence = LockSequence
	 * AVG cost 46.7 milliseconds, rounds = 1000, tasks = 1000, sequence = LockSequence
	 * AVG cost 39.5 milliseconds, rounds = 10000, tasks = 100, sequence = LockSequence
	 * AVG cost 40.4 milliseconds, rounds = 20000, tasks = 50, sequence = LockSequence
	 * AVG cost 39.8 milliseconds, rounds = 100000, tasks = 10, sequence = LockSequence
	 */
	public void test4Lock(int rounds, int tasks) throws Exception {
		long totalTimeMillis = 0;
		int count = 10;
		for(int i = 0; i < count; i++){
			totalTimeMillis += doTest(new LockSequence(), rounds, tasks);
		}
		System.out.println("AVG cost " + (totalTimeMillis * 1.0 / count) + " milliseconds, rounds = " + rounds + ", tasks = " + tasks + ", sequence = LockSequence");
	}
	
	/**
	 * AVG cost 7778.7 milliseconds, rounds = 10000, tasks = 10000, sequence = AtomicSequence
	 * AVG cost 7885.6 milliseconds, rounds = 100000, tasks = 1000, sequence = AtomicSequence
	 * AVG cost 8056.0 milliseconds, rounds = 200000, tasks = 500, sequence = AtomicSequence
	 * 
	 * 
	 * AVG cost 56.9 milliseconds, rounds = 100, tasks = 10000, sequence = AtomicSequence
	 * AVG cost 59.1 milliseconds, rounds = 1000, tasks = 1000, sequence = AtomicSequence
	 * AVG cost 69.8 milliseconds, rounds = 10000, tasks = 100, sequence = AtomicSequence
	 * AVG cost 64.7 milliseconds, rounds = 20000, tasks = 50, sequence = AtomicSequence
	 * AVG cost 70.4 milliseconds, rounds = 100000, tasks = 10, sequence = AtomicSequence
	 */
	public void test4Atomic(int rounds, int tasks) throws Exception {
		long totalTimeMillis = 0;
		int count = 10;
		for(int i = 0; i < count; i++){
			totalTimeMillis += doTest(new AtomicSequence(), rounds, tasks);
		}
		System.out.println("AVG cost " + (totalTimeMillis * 1.0 / count) + " milliseconds, rounds = " + rounds + ", tasks = " + tasks + ", sequence = AtomicSequence");
	}
	
}

class SynchronizationPerformanceCommand implements Runnable {
	
	private final Sequence sequence;
	
	private final CountDownLatch countDownLatch;
	
	private final int rounds;
	
	
	public SynchronizationPerformanceCommand(Sequence sequence, CountDownLatch countDownLatch, int rounds) {
		this.sequence = sequence;
		this.countDownLatch = countDownLatch;
		this.rounds = rounds;
	}

	public void run() {
		try {
			for (int i = 0; i < rounds; i++) {
				sequence.getNextSequenceValue();
			}
		} finally {
			countDownLatch.countDown();
		}
	}
	
}

interface Sequence {
	
	long getNextSequenceValue();
	
	long getCurrSequenceValue();
	
}

class SynchronizedSequence implements Sequence {

	private long value = 0;
	
	public synchronized long getNextSequenceValue() {
		return ++value;
	}

	public long getCurrSequenceValue() {
		return value;
	}
	
}

class LockSequence implements Sequence {

	private final ReentrantLock lock = new ReentrantLock();
	
	private long value = 0;
	
	public long getNextSequenceValue() {
		try {
			lock.lock();
			return ++value;
		} finally {
			lock.unlock();
		}
	}
	
	public long getCurrSequenceValue() {
		return value;
	}
	
}

class AtomicSequence implements Sequence {

	private final AtomicLong value = new AtomicLong();
	
	public long getNextSequenceValue() {
		return value.incrementAndGet();
	}
	
	public long getCurrSequenceValue() {
		return value.get();
	}
	
}