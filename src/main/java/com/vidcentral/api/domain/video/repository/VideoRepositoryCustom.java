package com.vidcentral.api.domain.video.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vidcentral.api.domain.video.model.Category;
import com.vidcentral.api.dto.video.response.VideoDetailResponse;
import com.vidcentral.api.dto.video.response.VideoResponse;

public interface VideoRepositoryCustom {

	List<VideoResponse> findRecommendedVideoResponsesBy(
		List<Category> categories, List<String> tags, List<Long> excludedVideoIds, Pageable pageable);

	Page<VideoResponse> findVideoResponseByKeyword(String keyword, Pageable pageable);

	Optional<VideoDetailResponse> findVideoDetailWithUploader(Long videoId);
}
