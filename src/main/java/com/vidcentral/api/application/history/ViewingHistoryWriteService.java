package com.vidcentral.api.application.history;

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

	public void saveViewingHistory(Long memberId, Long videoId) {
		final Member member = memberReadService.getMemberById(memberId);
		final Video video = videoReadService.getVideoById(videoId);
		final ViewingHistory viewingHistory = ViewingHistory.createViewingHistory(member, video);
		viewingHistoryRepository.save(viewingHistory);
	}

	public void updateViewingHistory(ViewingHistory viewingHistory, UpdateHistoryRequest updateHistoryRequest) {
		viewingHistory.updateViewingHistory(updateHistoryRequest.lastWatchedTime(), updateHistoryRequest.isFinished());
	}
}
