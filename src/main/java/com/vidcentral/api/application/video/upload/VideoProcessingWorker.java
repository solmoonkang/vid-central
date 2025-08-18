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

	private final VideoReadService videoReadService;
	private final FFmpegService fFmpegService;
	private final FileStorageService fileStorageService;

	@RabbitListener(queues = VIDEO_PROCESSING_QUEUE_NAME)
	@Transactional
	public void handleVideoProcessing(VideoProcessingMessage videoProcessingMessage) {
		final Video video = videoReadService.findVideoById(videoProcessingMessage.videoId());
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
