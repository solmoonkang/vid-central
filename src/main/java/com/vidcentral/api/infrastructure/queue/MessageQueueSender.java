package com.vidcentral.api.infrastructure.queue;

import static com.vidcentral.global.common.util.MessageQueueConstant.*;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.vidcentral.api.dto.video.response.VideoProcessingMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageQueueSender {

	/**
	 * MessageQueueSender는 RabbitMQ라는 메시지 큐 시스템에 메시지를 전송하는 역할을 한다.
	 * RabbitTemplate를 사용하여 video-processing-queue에 비디오 처리 요청 메시지를 보낸다.
	 * 이 클래스는 비동기 작업을 시작하는 트리거 역할을 하며, 애플리케이션의 동기적인 부분과 비동기적인 부분을 분리해주는 역할을 한다.
	 */

	private final RabbitTemplate rabbitTemplate;

	public void sendMessage(VideoProcessingMessage videoProcessingMessage) {
		rabbitTemplate.convertAndSend(VIDEO_PROCESSING_QUEUE_NAME, videoProcessingMessage);
	}
}
