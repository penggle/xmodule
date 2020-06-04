package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.example5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

import com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java.AbstractRabbitMqExample;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * topic消息示例
 * 
 * 
 * @author 	pengpeng
 * @date 	2020年5月16日 下午4:12:31
 */
public class Example5PlainJavaProducer extends AbstractRabbitMqExample {

	private final String exchangeName = "plainjava_topic_exchange";
	
	private final Map<String,List<String>> topicSkills = new LinkedHashMap<String,List<String>>();
	
	private final Random random = new Random();
	
	public Example5PlainJavaProducer() {
		topicSkills.put("os", Arrays.asList("Windows", "MacOS", "Linux"));
		topicSkills.put("db", Arrays.asList("MySQL", "Oracle", "PostgreSQL"));
		topicSkills.put("lang", Arrays.asList("C", "C++", "Java", "Go", "Python", "JavaScript"));
	}

	@Override
	public void run() throws Exception {
		ConnectionFactory connectionFactory = getConnectionFactory();
		//创建连接，创建通道
		try (Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(exchangeName, "topic"); //定义指定名称的topic类型的exchange
			for(int i = 0; i < 50; i++) {
				String[] message = getMessage();
				channel.basicPublish(exchangeName, message[0], null, message[1].getBytes()); //发布消息
				System.out.println("【Example4Producer】sent：" + message[0] + " - " + message[1]);
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
			}
		}
	}
	
	protected String[] getMessage() {
		List<String> topics = new ArrayList<>(topicSkills.keySet());
		topics.add("#");
		String messageTopic = topics.get(random.nextInt(topics.size()));
		final List<String> messageSkills = new ArrayList<>();
		if(messageTopic.equals("#")) {
			topicSkills.entrySet().stream().flatMap(entry -> entry.getValue().stream()).collect(Collectors.toCollection(() -> messageSkills));
			messageTopic = "itskills." + messageTopic;
		} else {
			List<String> skills = new ArrayList<>(topicSkills.get(messageTopic));
			skills.add("#");
			String skill = skills.get(random.nextInt(skills.size()));
			if(skill.equals("#")) {
				messageSkills.addAll(skills);
				messageTopic = "itskills." + messageTopic + ".#";
			} else {
				messageSkills.add(skill);
				messageTopic = "itskills." + messageTopic + "." + skill;
			}
		}
		return new String[] {messageTopic, messageSkills.stream().collect(Collectors.joining(","))};
	}
	
	public static void main(String[] args) throws Exception {
		new Example5PlainJavaProducer().run();
	}

}
