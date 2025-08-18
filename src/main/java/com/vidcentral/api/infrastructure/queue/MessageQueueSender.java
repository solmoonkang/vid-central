package com.vidcentral.api.infrastructure.queue;

import static com.vidcentral.global.common.util.MessageQueueConstant.*;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.vidcentral.api.dto.video.response.VideoProcessingMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageQueueSender {

	private final RabbitTemplate rabbitTemplate;

	public void sendMessage(VideoProcessingMessage videoProcessingMessage) {
		rabbitTemplate.convertAndSend(VIDEO_PROCESSING_QUEUE_NAME, videoProcessingMessage);
	}
}
