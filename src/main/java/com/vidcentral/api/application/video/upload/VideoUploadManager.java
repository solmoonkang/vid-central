package com.vidcentral.api.application.video.upload;

import static com.vidcentral.global.common.util.VideoConstant.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.vidcentral.api.application.video.mapper.VideoMapper;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.domain.video.model.UploadStatus;
import com.vidcentral.api.dto.video.request.UploadChunkRequest;
import com.vidcentral.api.dto.video.response.VideoProcessingMessage;
import com.vidcentral.api.infrastructure.queue.MessageQueueSender;
import com.vidcentral.api.infrastructure.storage.FileStorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoUploadManager {

	private static final Map<Long, List<String>> uploadedChunks = new ConcurrentHashMap<>();

	private final FileStorageService fileStorageService;
	private final MessageQueueSender messageQueueSender;

	public void processVideoChunkUpload(Video video, UploadChunkRequest uploadChunkRequest) {
		final String filePath = fileStorageService.saveChunk(
			uploadChunkRequest.videoFile(), video.getId(), uploadChunkRequest.chunkNumber());

		uploadedChunks.computeIfAbsent(video.getId(), file -> new ArrayList<>()).add(filePath);

		if (isAllChunksUploaded(video.getId(), uploadChunkRequest.totalChunks()))
			handleAllChunksUploaded(video, uploadChunkRequest);
	}

	private boolean isAllChunksUploaded(Long videoId, int totalChunks) {
		return uploadedChunks.get(videoId).size() == totalChunks;
	}

	private void handleAllChunksUploaded(Video video, UploadChunkRequest uploadChunkRequest) {
		final String mergedFilePath = fileStorageService.mergeChunks(
			video.getId(), uploadedChunks.get(video.getId()), video.getFilePath());

		video.updateVideoFileInfo(uploadChunkRequest.originalFileName(), mergedFilePath, UploadStatus.UPLOADING);

		final List<String> resolutionsToProcess = getResolutionToProcess(video.getResolutions());
		final VideoProcessingMessage videoProcessingMessage = VideoMapper.toVideoProcessingMessage(
			video, mergedFilePath, uploadChunkRequest.originalFileName(), resolutionsToProcess);
		messageQueueSender.sendMessage(videoProcessingMessage);

		uploadedChunks.remove(video.getId());
	}

	private List<String> getResolutionToProcess(List<String> resolutions) {
		if (resolutions == null || resolutions.isEmpty())
			return List.of(RESOLUTION_480P_VALUE);

		return resolutions;
	}
}
