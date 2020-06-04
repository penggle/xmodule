package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.example3;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.AbstractRabbitMqExample;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 发布/订阅示例
 * 
 * 使用指定名称的fanout类型的exchange与随机名称的queue进行绑定，构建发布/订阅模式的示例
 * 
 * 
 * @author 	pengpeng
 * @date 	2020年5月16日 下午4:12:31
 */
public class Example3PlainJavaProducer extends AbstractRabbitMqExample {

	private final String exchangeName = "plainjava_pubsub_exchange";
	
	@Override
	public void run() throws Exception {
		ConnectionFactory connectionFactory = getConnectionFactory();
		//创建连接，创建通道
		try (Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(exchangeName, "fanout"); //定义指定名称的fanout类型的exchange
			for(int i = 0; i < 20; i++) {
				String message = "hello, now time is: " + DateTimeUtils.formatNow();
				channel.basicPublish(exchangeName, "", null, message.getBytes()); //发布消息
				System.out.println("【Example3Producer】sent：" + message);
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		new Example3PlainJavaProducer().run();
	}

}
