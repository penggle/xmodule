package com.penglecode.xmodule.common.boot.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 默认的Spring任务执行及任务调度配置
 * 
 * @author 	pengpeng
 * @date 	2020年5月4日 下午1:13:59
 */
@Configuration
@EnableAsync
@EnableScheduling
public class DefaultTaskExecutionSchedulingConfiguration extends AbstractSpringConfiguration {

	private static final int CORE_SIZE = Runtime.getRuntime().availableProcessors();
	
	/**
	 * 默认的执行线程池配置
	 */
	@Bean
	@ConditionalOnMissingBean
	public TaskExecutorCustomizer defaultTaskExecutorCustomizer() {
		return (taskExecutor) -> {
			taskExecutor.setAllowCoreThreadTimeOut(false); //是否允许核心线程超时
			taskExecutor.setCorePoolSize(CORE_SIZE); //核心线程数
			taskExecutor.setMaxPoolSize(CORE_SIZE * 2 + 1); //最大线程数
			taskExecutor.setKeepAliveSeconds(60); //除核心线程外的线程存活时间
			taskExecutor.setQueueCapacity(100); //如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
			taskExecutor.setThreadNamePrefix("default-task-"); //执行线程名字前缀
			taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); //拒绝策略,CallerRunsPolicy: 线程池满了则直接在调用者的主线程上立即运行
		};
	}
	
	/**
	 * 原样copy自#TaskExecutionAutoConfiguration
	 * 增加@Primary指定，用以解决：expected single matching bean but found 2: applicationTaskExecutor,taskScheduler
	 */
	@Primary
	@Bean(name = { TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME,
			AsyncAnnotationBeanPostProcessor.DEFAULT_TASK_EXECUTOR_BEAN_NAME })
	@ConditionalOnMissingBean(Executor.class)
	public ThreadPoolTaskExecutor applicationTaskExecutor(TaskExecutorBuilder builder) {
		return builder.build();
	}

	/**
	 * 默认的调度线程池配置
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public TaskSchedulerCustomizer defaultTaskSchedulerCustomizer() {
		return (taskScheduler) -> {
			taskScheduler.setPoolSize(CORE_SIZE); //任务调度线程池大小
			taskScheduler.setThreadNamePrefix("default-scheduling-"); //调度线程名字前缀
			taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); //拒绝策略,CallerRunsPolicy: 线程池满了则直接在调用者的主线程上立即运行
		};
	}
	
	/*@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new SimpleAsyncUncaughtExceptionHandler();
	}*/
	
}
