package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.example2;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.AbstractRabbitMqExample;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 竞争队列示例
 * 一个生产者多个消费者
 * 
 * @author 	pengpeng
 * @date 	2020年5月16日 下午4:12:31
 */
public class Example2PlainJavaProducer extends AbstractRabbitMqExample {

	private final String queueName = "plainjava_worker_queue";
	
	@Override
	public void run() throws Exception {
		Random random = new Random();
		ConnectionFactory connectionFactory = getConnectionFactory();
		//创建连接，创建通道
		try (Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
			channel.queueDeclare(queueName, false, false, false, null); //声明队列
			for(int i = 0; i < 10; i++) {
				String message = "hello, workload=" + random.nextInt(15);
				channel.basicPublish("", queueName, null, message.getBytes()); //发布消息
				System.out.println("【Example2Producer】sent：" + message);
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		new Example2PlainJavaProducer().run();
	}

}
