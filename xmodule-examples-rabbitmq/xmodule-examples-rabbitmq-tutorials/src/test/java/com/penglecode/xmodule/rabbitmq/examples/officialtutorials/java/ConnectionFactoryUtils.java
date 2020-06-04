package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java;

import com.penglecode.xmodule.rabbitmq.examples.exception.RabbitMqException;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionFactoryUtils {

	public static ConnectionFactory simpleConnectionFactory(String uri, String vhost) {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri(uri);
			factory.setVirtualHost(vhost);
			return factory;
		} catch (Exception e) {
			throw new RabbitMqException(e);
		}
	}
	
}
