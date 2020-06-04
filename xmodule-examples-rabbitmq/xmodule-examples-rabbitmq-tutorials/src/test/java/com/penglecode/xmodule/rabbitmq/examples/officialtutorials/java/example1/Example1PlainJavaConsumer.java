package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.example1;

import java.io.IOException;

import com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.AbstractRabbitMqExample;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * 最简队列示例
 * 一个生产者一个消费者
 * 
 * @author 	pengpeng
 * @date 	2020年5月16日 下午4:12:31
 */
public class Example1PlainJavaConsumer extends AbstractRabbitMqExample {

	private final String queueName = "plainjava_simplest_queue";
	
	@Override
	public void run() throws Exception {
		Connection connection = getConnectionFactory().newConnection(); //创建连接
		Channel channel = connection.createChannel(); //创建通道
		channel.queueDeclare(queueName, false, false, false, null); //声明队列
		
		//autoAck=true，即在consumer收到消息的时候立即异步发送ack确认
		channel.basicConsume(queueName, true, new SimpleDeliverCallback(), consumerTag -> {});
		System.out.println("【Example1Consumer】消费启动...");
	}
	
	public static void main(String[] args) throws Exception {
		new Example1PlainJavaConsumer().run();
		Thread.currentThread().join();
	}

	class SimpleDeliverCallback implements DeliverCallback {

		/**
		 * @param consumerTag 			- 内部生成的消费标签
		 * @param message.envelope		- 信封, 包含属性：deliveryTag(标签，全局自增的), redeliver(是否是重新投递), exchange(交换机名称), routingKey(队列名称)
		 * @param message.properties	- 消息属性
		 * @param message.body			- 消息内容
		 */
		@Override
		public void handle(String consumerTag, Delivery message) throws IOException {
			System.out.println("=====================================================================================");
	        System.out.println("【Example1Consumer】consumerTag：" + consumerTag);
	        System.out.println("【Example1Consumer】message.envelope：" + message.getEnvelope());
	        System.out.println("【Example1Consumer】message.properties：" + message.getProperties());
	        System.out.println("【Example1Consumer】message.body：" + new String(message.getBody()));
		}
		
	}
	
}
