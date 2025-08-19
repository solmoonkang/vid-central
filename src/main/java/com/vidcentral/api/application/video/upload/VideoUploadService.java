package com.vidcentral.api.application.video.upload;

import static com.vidcentral.global.common.util.VideoConstant.*;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vidcentral.api.application.video.mapper.VideoMapper;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.domain.video.model.UploadStatus;
import com.vidcentral.api.domain.video.repository.VideoUploadRedisRepository;
import com.vidcentral.api.dto.video.request.UploadChunkRequest;
import com.vidcentral.api.dto.video.response.VideoProcessingMessage;
import com.vidcentral.api.infrastructure.queue.MessageQueueSender;
import com.vidcentral.api.infrastructure.storage.FileStorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoUploadService {

	private final FileStorageService fileStorageService;
	private final MessageQueueSender messageQueueSender;
	private final VideoUploadRedisRepository videoUploadRedisRepository;

	public void handleChunkUpload(Video video, UploadChunkRequest uploadChunkRequest) {
		final Optional<Integer> storedTotalChunks = videoUploadRedisRepository.getTotalChunks(video.getId());

		if (storedTotalChunks.isEmpty())
			initializeUpload(video.getId(), uploadChunkRequest.totalChunks());

		saveVideoChunk(video.getId(), uploadChunkRequest);

		if (isAllChunksUploaded(video.getId(), uploadChunkRequest.totalChunks()))
			processFinalFile(video, uploadChunkRequest);
	}

	private void initializeUpload(Long videoId, int totalChunks) {
		videoUploadRedisRepository.saveTotalChunks(videoId, totalChunks);
		fileStorageService.createDirectories(
			Paths.get(VIDEO_STORAGE_PATH, TEMP_DIRECTORY_NAME, videoId.toString()).toString());
	}

	private void saveVideoChunk(Long videoId, UploadChunkRequest uploadChunkRequest) {
		final String filePath = fileStorageService.createChunk(
			uploadChunkRequest.videoFile(), videoId, uploadChunkRequest.chunkNumber());
		videoUploadRedisRepository.saveChunkPath(videoId, uploadChunkRequest.chunkNumber(), filePath);
	}

	private boolean isAllChunksUploaded(Long videoId, int totalChunks) {
		final Long uploadedCount = videoUploadRedisRepository.getUploadedChunkCount(videoId);
		return uploadedCount != null && uploadedCount == totalChunks;
	}

	private void processFinalFile(Video video, UploadChunkRequest uploadChunkRequest) {
		final List<String> sortedFilePaths = getSortedChunkFilePaths(video.getId());
		final String mergedFilePath = fileStorageService.createMergedFile(sortedFilePaths, video.getFilePath());
		cleanUpTempFile(video.getId(), sortedFilePaths);

		updateVideoAndQueueMessage(video, uploadChunkRequest, mergedFilePath);
	}

	private List<String> getSortedChunkFilePaths(Long videoId) {
		final Map<Integer, String> uploadedChunks = videoUploadRedisRepository.getUploadedChunks(videoId);
		return uploadedChunks.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.map(Map.Entry::getValue)
			.toList();
	}

	private void cleanUpTempFile(Long videoId, List<String> sortedFilePaths) {
		sortedFilePaths.forEach(fileStorageService::deleteFile);

		fileStorageService.deleteDirectory(
			Paths.get(VIDEO_STORAGE_PATH, TEMP_DIRECTORY_NAME, videoId.toString()).toString());

		videoUploadRedisRepository.deleteTotalChunks(videoId);
		videoUploadRedisRepository.deleteChunkPath(videoId);
	}

	private void updateVideoAndQueueMessage(Video video, UploadChunkRequest uploadChunkRequest, String mergedFilePath) {
		video.updateVideoFileInfo(uploadChunkRequest.originalFileName(), mergedFilePath, UploadStatus.UPLOADING);

		final List<String> resolutionsToProcess = getResolutionToProcess(video.getResolutions());
		final VideoProcessingMessage videoProcessingMessage = VideoMapper.toVideoProcessingMessage(
			video, mergedFilePath, uploadChunkRequest.originalFileName(), resolutionsToProcess);

		messageQueueSender.sendMessage(videoProcessingMessage);
	}

	private List<String> getResolutionToProcess(List<String> resolutions) {
		if (resolutions == null || resolutions.isEmpty())
			return List.of(RESOLUTION_480P_VALUE);

		return resolutions;
	}
}
