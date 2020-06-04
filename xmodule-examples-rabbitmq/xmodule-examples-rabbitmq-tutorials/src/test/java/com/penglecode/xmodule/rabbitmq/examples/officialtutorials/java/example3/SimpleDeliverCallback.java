package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.example3;

import java.io.IOException;

import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class SimpleDeliverCallback implements DeliverCallback {

	/**
	 * @param consumerTag 			- 内部生成的消费标签
	 * @param message.envelope		- 信封, 包含属性：deliveryTag(标签，全局自增的), redeliver(是否是重新投递), exchange(交换机名称), routingKey(队列名称)
	 * @param message.properties	- 消息属性
	 * @param message.body			- 消息内容
	 */
	@Override
	public void handle(String consumerTag, Delivery message) throws IOException {
		System.out.println("=====================================================================================");
        System.out.println("【Example3Consumer】consumerTag：" + consumerTag);
        System.out.println("【Example3Consumer】message.envelope：" + message.getEnvelope());
        System.out.println("【Example3Consumer】message.properties：" + message.getProperties());
        System.out.println("【Example3Consumer】message.body：" + new String(message.getBody()));
	}
	
}