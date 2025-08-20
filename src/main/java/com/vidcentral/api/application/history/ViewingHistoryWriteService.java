package com.vidcentral.api.application.history;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.application.video.VideoReadService;
import com.vidcentral.api.domain.history.entity.ViewingHistory;
import com.vidcentral.api.domain.history.repository.ViewingHistoryRepository;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.dto.history.request.UpdateHistoryRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewingHistoryWriteService {

	private final MemberReadService memberReadService;
	private final VideoReadService videoReadService;
	private final ViewingHistoryRepository viewingHistoryRepository;

	@CacheEvict(value = "recentHistory", key = "#memberId")
	public void saveViewingHistory(Long memberId, Long videoId) {
		final Member member = memberReadService.findMemberById(memberId);
		final Video video = videoReadService.findVideoById(videoId);
		final ViewingHistory viewingHistory = ViewingHistory.createViewingHistory(member, video);
		viewingHistoryRepository.save(viewingHistory);
	}

	public void updateViewingHistory(ViewingHistory viewingHistory, UpdateHistoryRequest updateHistoryRequest) {
		viewingHistory.updateViewingHistory(updateHistoryRequest.lastWatchedTime(), updateHistoryRequest.isFinished());
	}
}
