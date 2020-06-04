package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.springamqp.example1;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.penglecode.xmodule.rabbitmq.examples.officialtutorials.springamqp.AbstractSpringAmqpExample;

public class Example1SpringAmqpAsyncConsumer extends AbstractSpringAmqpExample {

	private final String queueName = "springamqp_simplest_queue";
	
	@Override
	public void run() throws Exception {
		System.out.println("【Example1Consumer】消费启动...");
		Thread.currentThread().join();
	}
	
	@RabbitListener(ackMode="NONE", queues=queueName)
	public void handleMessage(Message message) {
		System.out.println("=====================================================================================");
        System.out.println("【Example1Consumer】message.properties：" + message.getMessageProperties());
        System.out.println("【Example1Consumer】message.body：" + new String(message.getBody()));
	}
	
}
