package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.example3;

import com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.AbstractRabbitMqExample;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 发布/订阅示例
 * 
 * 使用指定名称的fanout类型的exchange与随机名称的queue进行绑定，构建发布/订阅模式的示例
 * 
 * 有一点需要注意的是：Consumer都是实时订阅的，即在Consumer连接到Server之前Server上该exchange收到的消息并不会投递给新上来的Consumer
 * 
 * @author 	pengpeng
 * @date 	2020年5月16日 下午4:12:31
 */
public class Example3PlainJavaConsumer1 extends AbstractRabbitMqExample {

	private final String exchangeName = "plainjava_pubsub_exchange";
	
	@Override
	public void run() throws Exception {
		Connection connection = getConnectionFactory().newConnection(); //创建连接
		Channel channel = connection.createChannel(); //创建通道
		
		channel.exchangeDeclare(exchangeName, "fanout"); //定义指定名称的fanout类型的exchange
		String queueName = channel.queueDeclare().getQueue(); //向RabbitMQ Server声明一个由Server随机生成的、非持久性的、排他的、只与当前连接相关的且退出时自动删除的队列
		channel.queueBind(queueName, exchangeName, ""); //绑定随机生成的queue与指定的exchange
		
		//autoAck=true，即在consumer收到消息的时候立即异步发送ack确认
		channel.basicConsume(queueName, true, new SimpleDeliverCallback(), consumerTag -> {});
		System.out.println("【Example3Consumer】消费启动...");
	}
	
	public static void main(String[] args) throws Exception {
		new Example3PlainJavaConsumer1().run();
		Thread.currentThread().join();
	}

}
