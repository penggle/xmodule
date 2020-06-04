package com.penglecode.xmodule.rabbitmq.examples.officialtutorials.java;

import com.rabbitmq.client.ConnectionFactory;

public abstract class AbstractRabbitMqExample {

	private final String uri = "amqp://flexedge-admin:123456@172.16.83.132:5672";
	
	private final String vhost = "flexedge";
	
	private final ConnectionFactory connectionFactory;
	
	public AbstractRabbitMqExample() {
		this.connectionFactory = createConnectionFactory();
	}
	
	protected ConnectionFactory createConnectionFactory() {
		return ConnectionFactoryUtils.simpleConnectionFactory(uri, vhost);
	}

	protected String getUri() {
		return uri;
	}

	protected String getVhost() {
		return vhost;
	}

	protected ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}
	
	public abstract void run() throws Exception;
	
}
