package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.example4;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.AbstractRabbitMqExample;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 路由消息示例
 * 
 * 使用指定名称的direct类型的exchange与随机名称的queue及routingKey进行绑定，构建路由消息示例
 * 
 * 
 * @author 	pengpeng
 * @date 	2020年5月16日 下午4:12:31
 */
public class Example4PlainJavaProducer extends AbstractRabbitMqExample {

	private final String exchangeName = "plainjava_routing_exchange";
	
	private final Random random = new Random();
	
	@Override
	public void run() throws Exception {
		ConnectionFactory connectionFactory = getConnectionFactory();
		//创建连接，创建通道
		try (Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(exchangeName, "direct"); //定义指定名称的direct类型的exchange
			for(int i = 0; i < 50; i++) {
				String[] message = getMessage();
				channel.basicPublish(exchangeName, message[0], null, message[1].getBytes()); //发布消息
				System.out.println("【Example4Producer】sent：" + message[0] + " - " + message[1]);
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
			}
		}
	}
	
	protected String[] getMessage() {
		String[] log = new String[2];
		int temperature = random.nextInt(50);
		if(temperature > 40) {
			log[0] = "error";
		} else if (temperature > 30) {
			log[0] = "warning";
		} else {
			log[0] = "info";
		}
		log[1] = "device temperature is : " + temperature;
		return log;
	}
	
	public static void main(String[] args) throws Exception {
		new Example4PlainJavaProducer().run();
	}

}
