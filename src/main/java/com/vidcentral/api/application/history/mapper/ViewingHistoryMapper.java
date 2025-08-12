package com.vidcentral.api.application.history.mapper;

import com.vidcentral.api.application.video.mapper.VideoMapper;
import com.vidcentral.api.domain.history.entity.ViewingHistory;
import com.vidcentral.api.dto.history.response.ViewingHistoryResponse;
import com.vidcentral.api.dto.video.response.VideoResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ViewingHistoryMapper {

	public static ViewingHistoryResponse toViewingHistoryResponse(ViewingHistory viewingHistory) {
		final VideoResponse videoResponse = VideoMapper.toVideoResponse(viewingHistory.getVideo());
		return new ViewingHistoryResponse(viewingHistory.getId(), viewingHistory.getLastWatchedTime(),
			viewingHistory.getIsFinished(), viewingHistory.getUpdatedAt(), videoResponse);
	}
}
