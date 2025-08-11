package com.vidcentral.api.application.recommend;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vidcentral.api.application.recommend.algorithm.RecommendationAlgorithm;
import com.vidcentral.api.domain.video.entity.Video;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationService {

	private final RecommendationAlgorithm recommendationAlgorithm;

	public List<Video> getRecommendedVideos(Long memberId) {
		if (memberId == null)
			return Collections.emptyList();

		return recommendationAlgorithm.recommend(memberId);
	}
}
