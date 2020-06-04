package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.springamqp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.penglecode.xmodule.rabbitmq.examples.boot.RabbitMqExampleApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE, classes=RabbitMqExampleApplication.class)
public abstract class AbstractSpringAmqpExample {

	@Autowired
	private AmqpAdmin amqpAdmin;
	
	@Autowired
	private AmqpTemplate amqpTemplate;

	public AmqpAdmin getAmqpAdmin() {
		return amqpAdmin;
	}

	public AmqpTemplate getAmqpTemplate() {
		return amqpTemplate;
	}
	
	@Test
	public abstract void run() throws Exception;
	
}
