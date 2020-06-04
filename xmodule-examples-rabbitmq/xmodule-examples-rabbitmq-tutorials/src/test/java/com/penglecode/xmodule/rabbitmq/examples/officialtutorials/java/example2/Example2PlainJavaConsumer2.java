package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.example2;

import com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.AbstractRabbitMqExample;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 竞争队列示例
 * 一个生产者多个消费者
 * 
 * 其中关闭了消息消费端的自动Ack确认，改为在DeliverCallback中手动Ack确认
 * 
 * 当Consumer接收到消息、处理任务完成之后，会发送带有这个消息标示符的ack，来告诉server这个消息接收到并处理完成。
 * RabbitMQ会一直等到处理某个消息的Consumer的链接失去之后，才确定这个消息没有正确处理，从而RabbitMQ重发这个消息。
 * 
 * @author 	pengpeng
 * @date 	2020年5月16日 下午4:12:31
 */
public class Example2PlainJavaConsumer2 extends AbstractRabbitMqExample {

	private final String queueName = "plainjava_worker_queue";
	
	@Override
	public void run() throws Exception {
		Connection connection = getConnectionFactory().newConnection(); //创建连接
		Channel channel = connection.createChannel(); //创建通道
		channel.queueDeclare(queueName, false, false, false, null); //声明队列
		
		channel.basicQos(1); //一次只接受一条未收到的消息,而且是在手动Ack确认被RabbitMQ Server收到之后才会发出下一个消息
		//autoAck=false，关闭自动Ack确认，改为在DeliverCallback中手动Ack确认
		channel.basicConsume(queueName, false, new Example2DeliverCallback(channel), consumerTag -> {});
		
		System.out.println("【Example2Consumer】消费启动...");
	}
	
	public static void main(String[] args) throws Exception {
		new Example2PlainJavaConsumer2().run();
		Thread.currentThread().join();
	}

}
