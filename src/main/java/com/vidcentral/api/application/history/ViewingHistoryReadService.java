package com.vidcentral.api.application.history;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vidcentral.api.domain.history.entity.ViewingHistory;
import com.vidcentral.api.domain.history.repository.ViewingHistoryRepository;
import com.vidcentral.global.error.exception.NotFoundException;
import com.vidcentral.global.error.model.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewingHistoryReadService {

	private final ViewingHistoryRepository viewingHistoryRepository;

	@Cacheable(value = "recentHistory", key = "#memberId")
	public List<ViewingHistory> findRecentViewingHistoriesBy(Long memberId, Pageable pageable) {
		return viewingHistoryRepository.findWithVideoByMemberId(memberId, pageable);
	}

	public Page<ViewingHistory> findPagedViewingHistoriesBy(Long memberId, Pageable pageable) {
		return viewingHistoryRepository.findPagedWithVideoByMemberId(memberId, pageable);
	}

	public ViewingHistory findViewingHistory(Long memberId, Long videoId) {
		return viewingHistoryRepository.findByMemberIdAndVideoId(memberId, videoId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.VIEWING_HISTORY_NOT_FOUND));
	}
}
