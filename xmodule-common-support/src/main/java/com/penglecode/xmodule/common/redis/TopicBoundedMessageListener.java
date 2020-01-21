package com.penglecode.xmodule.common.redis;

import org.springframework.data.redis.connection.MessageListener;

public interface TopicBoundedMessageListener extends MessageListener {

	public String getBoundedMessageTopic();
	
}
