package com.penglecode.xmodule.common.support;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * named thread factory
 * 
 * @author  pengpeng
 * @date 	 2014年5月22日 上午10:04:43
 * @version 1.0
 */
public class NamedThreadFactory implements ThreadFactory {

	public static final String DEFAULT_THREAD_NAME_PREFIX = "threadPool";
	
	private static final AtomicInteger THREAD_POOL_NUMBER = new AtomicInteger(1);

	private static final String NAME_PATTERN = "%s-%d-thread";
	
	private final ThreadGroup group;
	
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	
	private final String threadNamePrefix;
	
	public NamedThreadFactory(String threadNamePrefix) {
		super();
		final SecurityManager sm = System.getSecurityManager();
		this.group = (sm != null) ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
		this.threadNamePrefix = String.format(NAME_PATTERN, checkThreadNamePrefix(threadNamePrefix), THREAD_POOL_NUMBER.getAndIncrement());
	}

	public String checkThreadNamePrefix(String threadNamePrefix) {
		if(threadNamePrefix == null || "".equals(threadNamePrefix.trim())){
			return DEFAULT_THREAD_NAME_PREFIX;
		}
		return threadNamePrefix;
	}
	
	public Thread newThread(Runnable r) {
		final Thread thread = new Thread(group, r, String.format("%s-%d", threadNamePrefix, threadNumber.getAndIncrement()), 0);
		thread.setDaemon(false);
		thread.setPriority(Thread.NORM_PRIORITY);
		return thread;
	}

}
