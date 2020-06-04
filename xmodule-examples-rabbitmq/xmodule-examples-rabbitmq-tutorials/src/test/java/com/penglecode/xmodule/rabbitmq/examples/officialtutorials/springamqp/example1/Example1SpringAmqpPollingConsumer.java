package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.springamqp.example1;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;

import com.penglecode.xmodule.rabbitmq.examples.officialtutorials.springamqp.AbstractSpringAmqpExample;

public class Example1SpringAmqpPollingConsumer extends AbstractSpringAmqpExample {

	private final String queueName = "springamqp_simplest_queue";
	
	@Override
	public void run() throws Exception {
		getAmqpAdmin().declareQueue(new Queue(queueName, false, false, false, null));
		System.out.println("【Example1Consumer】消费启动...");
		while(true) {
			Message message = getAmqpTemplate().receive(queueName, 500);
			if(message != null) {
				System.out.println("=====================================================================================");
		        System.out.println("【Example1Consumer】message.properties：" + message.getMessageProperties());
		        System.out.println("【Example1Consumer】message.body：" + new String(message.getBody()));
			}
		}
	}
	
}
