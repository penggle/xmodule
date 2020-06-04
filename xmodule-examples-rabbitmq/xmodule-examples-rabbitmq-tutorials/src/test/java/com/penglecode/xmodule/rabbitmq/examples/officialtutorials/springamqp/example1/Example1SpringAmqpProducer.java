package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.springamqp.example1;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.Queue;

import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.rabbitmq.examples.officialtutorials.springamqp.AbstractSpringAmqpExample;

public class Example1SpringAmqpProducer extends AbstractSpringAmqpExample {

	private final String queueName = "springamqp_simplest_queue";

	@Override
	public void run() throws Exception {
		getAmqpAdmin().declareQueue(new Queue(queueName, false, false, false, null));
		for(int i = 0; i < 10; i++) {
			String content = "hello, now time is: " + DateTimeUtils.formatNow();
			getAmqpTemplate().send(queueName, MessageBuilder.withBody(content.getBytes()).build());
			System.out.println("【Example1Producer】sent：" + content);
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
		}
	}
	
}
