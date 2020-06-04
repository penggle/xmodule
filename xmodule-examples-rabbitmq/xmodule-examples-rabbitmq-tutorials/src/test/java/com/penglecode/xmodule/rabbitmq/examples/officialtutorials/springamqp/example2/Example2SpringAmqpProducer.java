package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.springamqp.example2;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.Queue;

import com.penglecode.xmodule.rabbitmq.examples.officialtutorials.springamqp.AbstractSpringAmqpExample;

public class Example2SpringAmqpProducer extends AbstractSpringAmqpExample {

	private final String queueName = "springamqp_worker_queue";

	@Override
	public void run() throws Exception {
		Random random = new Random();
		getAmqpAdmin().declareQueue(new Queue(queueName, false, false, false, null));
		for(int i = 0; i < 10; i++) {
			String content = "hello, workload=" + random.nextInt(15);
			getAmqpTemplate().send(queueName, MessageBuilder.withBody(content.getBytes()).build());
			System.out.println("【Example2Producer】sent：" + content);
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
		}
	}
	
}
