package com.vidcentral.api.application.recommend.algorithm;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.vidcentral.api.application.history.ViewingHistoryReadService;
import com.vidcentral.api.application.video.VideoReadService;
import com.vidcentral.api.domain.history.entity.ViewingHistory;
import com.vidcentral.api.domain.video.model.Category;
import com.vidcentral.api.dto.video.response.VideoResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserBasedRecommendationAlgorithm implements RecommendationAlgorithm {

	public static final int RECENT_HISTORY_COUNT = 10;
	public static final int RECOMMENDED_VIDEO_COUNT = 20;

	private final VideoReadService videoReadService;
	private final ViewingHistoryReadService viewingHistoryReadService;

	@Override
	public List<VideoResponse> recommend(Long memberId) {
		final List<ViewingHistory> recentHistories = viewingHistoryReadService.findRecentViewingHistoriesBy(
			memberId, PageRequest.of(0, RECENT_HISTORY_COUNT));

		if (recentHistories.isEmpty())
			return Collections.emptyList();

		final List<Category> recentCategories = extractCategoriesFromHistories(recentHistories);
		final List<String> recentTags = extractTagsFromHistories(recentHistories);

		final List<Long> watchedVideoIds = recentHistories.stream()
			.map(history -> history.getVideo().getId())
			.toList();

		return videoReadService.findRecommendedVideoResponses(
			recentCategories, recentTags, watchedVideoIds, PageRequest.of(0, RECOMMENDED_VIDEO_COUNT));
	}

	private List<Category> extractCategoriesFromHistories(List<ViewingHistory> viewingHistories) {
		return viewingHistories.stream()
			.map(history -> history.getVideo().getCategory())
			.distinct()
			.toList();
	}

	private List<String> extractTagsFromHistories(List<ViewingHistory> viewingHistories) {
		return viewingHistories.stream()
			.map(ViewingHistory::getVideo)
			.flatMap(video -> video.getTags().stream())
			.distinct()
			.toList();
	}
}
