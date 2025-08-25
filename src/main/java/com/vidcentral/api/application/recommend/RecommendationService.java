package com.vidcentral.api.application.recommend;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vidcentral.api.application.recommend.algorithm.RecommendationAlgorithm;
import com.vidcentral.api.dto.video.response.VideoResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationService {

	private final RecommendationAlgorithm recommendationAlgorithm;

	public List<VideoResponse> getRecommendedVideos(Long memberId) {
		return Optional.ofNullable(memberId)
			.map(recommendationAlgorithm::recommend)
			.orElseGet(Collections::emptyList);
	}
}
