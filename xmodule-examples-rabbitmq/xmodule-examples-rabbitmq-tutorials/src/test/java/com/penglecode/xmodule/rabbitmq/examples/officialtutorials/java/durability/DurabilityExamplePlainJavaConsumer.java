package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.durability;

import java.io.IOException;

import com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.AbstractRabbitMqExample;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * https://www.rabbitmq.com/tutorials/tutorial-two-java.html
 * 
 * 消息持久性保证：
 * 1、在channel.queueDeclare(...)队列声明方法中将durable设置为true，此设置保证RabbitMQ Server重启之后队列将会被恢复而不会丢失
 * 2、在channel.basicPublish(...)发布消息方法中将properties设置为MessageProperties.PERSISTENT_TEXT_PLAIN确保消息持久性，
 * 	  但这也不能保证绝对可靠的消息持久，因为在RabbitMQ Server收到消息到保存消息之间的窗口时间内如果Server挂了，仍然会丢失消息。
 *    此外，尽管如此设置，RabbitMQ Server也不会对每条消息执行fsync(2)保存消息，有可能将仅仅是保存到缓存中而不是保存到磁盘中。
 *    
 * 总的来说，以上两点的持久性保证并不强，但对于我们的简单任务队列来说已经足够了。如果您需要更强的保证，那么您可以使用publisher confirm {@link #https://www.rabbitmq.com/confirms.html}
 * 
 * @author 	pengpeng
 * @date 	2020年5月16日 下午4:12:31
 */
public class DurabilityExamplePlainJavaConsumer extends AbstractRabbitMqExample {

	private final String queueName = "plainjava_durability_queue";
	
	@Override
	public void run() throws Exception {
		Connection connection = getConnectionFactory().newConnection(); //创建连接
		Channel channel = connection.createChannel(); //创建通道
		channel.queueDeclare(queueName, true, false, false, null); //声明队列
		
		//autoAck=true，即在consumer收到消息的时候立即异步发送ack确认
		channel.basicConsume(queueName, true, new SimpleDeliverCallback(), consumerTag -> {});
		System.out.println("【DurabilityConsumer】消费启动...");
	}
	
	public static void main(String[] args) throws Exception {
		new DurabilityExamplePlainJavaConsumer().run();
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
