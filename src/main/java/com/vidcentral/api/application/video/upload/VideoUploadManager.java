package com.vidcentral.api.application.video.upload;

import static com.vidcentral.global.common.util.VideoConstant.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vidcentral.api.application.video.mapper.VideoMapper;
import com.vidcentral.api.dto.video.request.UploadVideoRequest;
import com.vidcentral.api.dto.video.response.VideoProcessingMessage;
import com.vidcentral.api.infrastructure.queue.MessageQueueSender;
import com.vidcentral.api.infrastructure.storage.FileStorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoUploadManager {

	/**
	 * VideoUploadManager는 비디오 업로드의 초기 과정을 총괄하는 클래스이다.
	 * 시용자가 보낸 파일을 임시로 저장하고, 비디오 처리 작업을 메시지 큐에 전달하는 역할을 한다.
	 * 이 클래스는 동기적인 작업 (파일 저장)과 비동기적인 작업 (메시지 큐 전송)을 결합하여, HTTP 요청-응답 주기 내에 완료되도록 만든다.
	 */

	private final FileStorageService fileStorageService;
	private final MessageQueueSender messageQueueSender;

	public void processVideoUpload(Long videoId, UploadVideoRequest uploadVideoRequest) {
		final String filePath = fileStorageService.saveFile(uploadVideoRequest.videoFile());
		final List<String> resolutionsToProcess = getResolutionToProcess(uploadVideoRequest.resolutions());

		final VideoProcessingMessage videoProcessingMessage = VideoMapper.toVideoProcessingMessage(
			videoId, filePath, uploadVideoRequest, resolutionsToProcess);
		messageQueueSender.sendMessage(videoProcessingMessage);
	}

	private List<String> getResolutionToProcess(List<String> resolutions) {
		if (resolutions == null || resolutions.isEmpty())
			return List.of(RESOLUTION_480P_VALUE);

		return resolutions;
	}
}
