package com.vidcentral.api.application.video.upload;

import static com.vidcentral.global.common.util.MessageQueueConstant.*;

import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.video.VideoReadService;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.domain.video.model.UploadStatus;
import com.vidcentral.api.dto.video.response.VideoProcessingMessage;
import com.vidcentral.api.infrastructure.storage.FileStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoProcessingWorker {

	/**
	 * VideoProcessingWorker는 비동기 처리의 핵심 역할을 맡는다.
	 *
	 * @RabbitListener 어노테이션을 통해 메시지 큐에 들어온 메시지를 자동으로 수신하고, 실제 비디오 처리 작업을 시작한다.
	 * 이 클래스는 메시지로부터 비디오 정보를 얻어 DB 상태를 PROCESSING으로 변경하고, FFmpegService를 호출하여 인코딩과 썸네일 생성을 진행한다.
	 * 작업이 끝나면 상태를 COMPLETE로 바꾸고, 오류 발생 시 FAILED로 처리하여 유실되는 작업이 없도록 보장한다.
	 */

	private final VideoReadService videoReadService;
	private final FFmpegService fFmpegService;
	private final FileStorageService fileStorageService;

	@RabbitListener(queues = VIDEO_PROCESSING_QUEUE_NAME)
	@Transactional
	public void handleVideoProcessing(VideoProcessingMessage videoProcessingMessage) {
		final Video video = videoReadService.getVideoById(videoProcessingMessage.videoId());
		final String originalFilePath = videoProcessingMessage.filePath();

		try {
			video.updateUploadStatus(UploadStatus.PROCESSING);

			final String baseFileName = UUID.randomUUID().toString();
			final List<String> processedFilePaths = fFmpegService.processVideo(
				originalFilePath, videoProcessingMessage.resolutions(), baseFileName);
			final String thumbnailPath = fFmpegService.createThumbnail(originalFilePath, baseFileName);

			video.updateOriginalFileName(videoProcessingMessage.originalFileName());
			video.updateFilePath(processedFilePaths);
			video.updateThumbnailPath(thumbnailPath);
			video.updateUploadStatus(UploadStatus.COMPLETE);
		} catch (Exception e) {
			video.updateUploadStatus(UploadStatus.FAILED);
			log.warn("[✅ LOGGER] 비디오 처리 중 오류가 발생했습니다.", e);
		}

		fileStorageService.deleteFile(originalFilePath);
	}
}
