package com.vidcentral.api.application.video;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.domain.video.model.Category;
import com.vidcentral.api.domain.video.repository.VideoRepository;
import com.vidcentral.api.dto.video.response.VideoDetailResponse;
import com.vidcentral.api.dto.video.response.VideoResponse;
import com.vidcentral.global.error.exception.NotFoundException;
import com.vidcentral.global.error.model.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoReadService {

	private final VideoRepository videoRepository;

	public Video findVideoById(Long videoId) {
		return videoRepository.findById(videoId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.VIDEO_NOT_FOUND));
	}

	public VideoDetailResponse findVideoDetailResponseById(Long videoId) {
		return videoRepository.findVideoDetailWithUploader(videoId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.VIDEO_NOT_FOUND));
	}

	public List<Video> findRecommendedVideoBy(List<Category> categories, List<String> tags, List<Long> videoIds,
		Pageable pageable) {
		return videoRepository.findRecommendedVideos(categories, tags, videoIds, pageable);
	}

	@Cacheable(value = "videoSearch", key = "#keyword + '::' + #pageable.pageNumber")
	public Page<VideoResponse> searchVideos(String keyword, Pageable pageable) {
		return videoRepository.findVideoResponseByKeyword(keyword, pageable);
	}
}
