package com.vidcentral.api.application.video;

import org.springframework.stereotype.Service;

import com.vidcentral.api.application.video.upload.VideoUploadService;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.domain.video.repository.VideoRepository;
import com.vidcentral.api.dto.video.request.CreateVideoRequest;
import com.vidcentral.api.dto.video.request.UpdateVideoRequest;
import com.vidcentral.api.dto.video.request.UploadChunkRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoWriteService {

	private final VideoUploadService videoUploadService;
	private final VideoRepository videoRepository;

	public Video saveVideoMetadata(Member member, CreateVideoRequest createVideoRequest) {
		final Video video = Video.createVideoMetadata(
			member, createVideoRequest.title(), createVideoRequest.description(), createVideoRequest.resolutions(),
			createVideoRequest.category(), createVideoRequest.tags());

		return videoRepository.save(video);
	}

	public void uploadVideoChunk(Video video, UploadChunkRequest uploadChunkRequest) {
		videoUploadService.handleChunkUpload(video, uploadChunkRequest);
	}

	public void incrementViewCount(Long videoId) {
		videoRepository.incrementViewCount(videoId);
	}

	public void updateVideo(Video video, UpdateVideoRequest updateVideoRequest) {
		video.updateVideo(updateVideoRequest.title(), updateVideoRequest.description(),
			updateVideoRequest.thumbnailPath(), updateVideoRequest.category(), updateVideoRequest.tags());
	}

	public void deleteVideo(Long videoId) {
		videoRepository.deleteById(videoId);
	}
}
