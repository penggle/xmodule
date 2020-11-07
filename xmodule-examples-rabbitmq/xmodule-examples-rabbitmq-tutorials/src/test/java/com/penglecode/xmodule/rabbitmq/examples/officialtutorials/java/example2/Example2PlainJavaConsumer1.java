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
 * RabbitMQ会一直等到处理某个消息的Consumer的连接失去之后，才确定这个消息没有正确处理，从而RabbitMQ重发这个消息。
 * 
 * 在Consumer运行过程中，如果强制关闭某个Consumer【Java Application】(即失去连接)，则即可验证上面所述，即会在其他Consumer中会出现redeliver=true的重新投递消息
 * 
 * @author 	pengpeng
 * @date 	2020年5月16日 下午4:12:31
 */
public class Example2PlainJavaConsumer1 extends AbstractRabbitMqExample {

	private final String queueName = "plainjava_worker_queue";
	
	@Override
	public void run() throws Exception {
		Connection connection = getConnectionFactory().newConnection(); //创建连接
		Channel channel = connection.createChannel(); //创建通道
		channel.queueDeclare(queueName, false, false, false, null); //声明队列
		
		channel.basicQos(1); //一次只接受一条未收到的消息,而且是在手动Ack确认被RabbitMQ Server收到之后才会发出下一个消息
		//autoAck=true，即noack=true，即客户端没有确认，服务端默认为客户端成功地消费到了消息，并将消息从服务端删除
		channel.basicConsume(queueName, false, new Example2DeliverCallback(channel), consumerTag -> {});
		
		System.out.println("【Example2Consumer】消费启动...");
	}
	
	public static void main(String[] args) throws Exception {
		new Example2PlainJavaConsumer1().run();
		Thread.currentThread().join();
	}

}
