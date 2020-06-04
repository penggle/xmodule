package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.springamqp.example2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.penglecode.xmodule.rabbitmq.examples.officialtutorials.springamqp.AbstractSpringAmqpExample;
import com.rabbitmq.client.Channel;

public class Example2SpringAmqpAsyncConsumer2 extends AbstractSpringAmqpExample {

	private final String queueName = "springamqp_worker_queue";
	
	@Override
	public void run() throws Exception {
		System.out.println("【Example2Consumer】消费启动...");
		Thread.currentThread().join();
	}
	
	@RabbitListener(ackMode="MANUAL", queuesToDeclare = @Queue(name=queueName, durable="false", exclusive="false", autoDelete="false"))
	public void handleMessage(Message message, Channel channel) throws Exception {
		try {
			String body = new String(message.getBody());
			System.out.println("=====================================================================================");
	        System.out.println("【Example2Consumer】message.properties：" + message.getMessageProperties());
	        System.out.println("【Example2Consumer】message.body：" + body);
	        System.out.println("【Example2Consumer】>>> message received");
	        Long workload = Long.valueOf(body.split("=")[1]);
	        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(workload));
	        System.out.println("【Example2Consumer】<<< message consumed");
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //手动Ack
		}
	}
	
}
