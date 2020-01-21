package com.penglecode.xmodule.common.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import com.penglecode.xmodule.common.consts.ApplicationConstants;

/**
 * 在将来执行某个任务的工具类
 * 
 * @author 	pengpeng
 * @date	2019年7月26日 下午5:40:55
 */
public class AsyncExecuteUtils {

	private static final Executor DEFAULT_EXECUTOR = ApplicationConstants.DEFAULT_EXECUTOR;
	
	public static void asyncExecute(Runnable task) {
		CompletableFuture.runAsync(task, DEFAULT_EXECUTOR);
	}
	
	public static void asyncLazyExecute(Runnable task, int delay, TimeUnit unit) {
		CompletableFuture.completedFuture(delay).thenRunAsync(() -> {
			LockSupport.parkNanos(unit.toNanos(delay));
		}, DEFAULT_EXECUTOR).thenRun(task);
	}
	
}
