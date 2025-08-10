package com.vidcentral.global.config;

import static com.vidcentral.global.common.util.MessageQueueConstant.*;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Bean
	public Queue videoProcessingQueue() {
		return new Queue(VIDEO_PROCESSING_QUEUE_NAME, true);
	}
}
