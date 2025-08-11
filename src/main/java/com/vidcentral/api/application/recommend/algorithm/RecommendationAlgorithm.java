package com.vidcentral.api.application.recommend.algorithm;

import java.util.List;

import com.vidcentral.api.domain.video.entity.Video;

public interface RecommendationAlgorithm {

	List<Video> recommend(Long memberId);
}
