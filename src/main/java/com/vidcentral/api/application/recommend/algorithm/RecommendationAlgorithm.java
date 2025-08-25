package com.vidcentral.api.application.recommend.algorithm;

import java.util.List;

import com.vidcentral.api.dto.video.response.VideoResponse;

public interface RecommendationAlgorithm {

	List<VideoResponse> recommend(Long memberId);
}
