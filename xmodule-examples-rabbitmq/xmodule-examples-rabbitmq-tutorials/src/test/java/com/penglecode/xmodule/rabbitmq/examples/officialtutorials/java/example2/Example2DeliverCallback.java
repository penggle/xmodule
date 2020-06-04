package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.example2;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Example2DeliverCallback implements DeliverCallback {

	private final Channel channel;
	
	public Example2DeliverCallback(Channel channel) {
		super();
		this.channel = channel;
	}

	/**
	 * @param consumerTag 			- 内部生成的消费标签
	 * @param message.envelope		- 信封, 包含属性：deliveryTag(标签，全局自增的), redeliver(是否是重新投递), exchange(交换机名称), routingKey(队列名称)
	 * @param message.properties	- 消息属性
	 * @param message.body			- 消息内容
	 */
	@Override
	public void handle(String consumerTag, Delivery message) throws IOException {
		try {
			String body = new String(message.getBody());
			System.out.println("=====================================================================================");
			System.out.println("【Example2Consumer】>>> consumerTag：" + consumerTag);
	        System.out.println("【Example2Consumer】>>> message.envelope：" + message.getEnvelope());
	        System.out.println("【Example2Consumer】>>> message.properties：" + message.getProperties());
	        System.out.println("【Example2Consumer】>>> message.body：" + body);
	        System.out.println("【Example2Consumer】>>> message received");
	        Long workload = Long.valueOf(body.split("=")[1]);
	        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(workload));
	        System.out.println("【Example2Consumer】<<< message consumed");
		} finally {
			channel.basicAck(message.getEnvelope().getDeliveryTag(), false); //手动Ack
		}
	}

}
