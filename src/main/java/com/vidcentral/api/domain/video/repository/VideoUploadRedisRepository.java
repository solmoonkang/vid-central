package com.vidcentral.api.domain.video.repository;

import static com.vidcentral.global.common.util.RedisConstant.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.vidcentral.api.infrastructure.redis.HashRedisRepository;
import com.vidcentral.api.infrastructure.redis.ValueRedisRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VideoUploadRedisRepository {

	private final ValueRedisRepository valueRedisRepository;
	private final HashRedisRepository hashRedisRepository;

	public void saveTotalChunks(Long videoId, int totalChunks) {
		valueRedisRepository.set(generateTotalChunksKey(videoId), String.valueOf(totalChunks));
	}

	public void saveChunkPath(Long videoId, int chunkNumber, String filePath) {
		hashRedisRepository.put(generateVideoChunkKey(videoId), String.valueOf(chunkNumber), filePath);
	}

	public Optional<Integer> getTotalChunks(Long videoId) {
		final String totalChunks = valueRedisRepository.get(generateTotalChunksKey(videoId));
		return Optional.ofNullable(totalChunks).map(Integer::valueOf);
	}

	public Map<Integer, String> getUploadedChunks(Long videoId) {
		final Map<Object, Object> filePathEntries = hashRedisRepository.getEntries(generateVideoChunkKey(videoId));
		final Map<Integer, String> uploadedFilePaths = new HashMap<>();
		filePathEntries.forEach((key, value) -> uploadedFilePaths.put(Integer.valueOf(key.toString()), (String)value));
		return uploadedFilePaths;
	}

	public Long getUploadedChunkCount(Long videoId) {
		return hashRedisRepository.getSize(generateVideoChunkKey(videoId));
	}

	public boolean hasChunk(Long videoId, int chunkNumber) {
		return hashRedisRepository.hasKey(generateVideoChunkKey(videoId), String.valueOf(chunkNumber));
	}

	public void deleteTotalChunks(Long videoId) {
		valueRedisRepository.delete(generateTotalChunksKey(videoId));
	}

	public void deleteChunkPath(Long videoId) {
		hashRedisRepository.delete(generateVideoChunkKey(videoId));
	}

	private String generateVideoChunkKey(Long videoId) {
		return VIDEO_CHUNK_PREFIX + videoId;
	}

	private String generateTotalChunksKey(Long videoId) {
		return TOTAL_CHUNKS_PREFIX + videoId;
	}
}
