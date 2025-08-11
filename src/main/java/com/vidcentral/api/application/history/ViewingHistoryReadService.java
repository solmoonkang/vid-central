package com.vidcentral.api.application.history;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vidcentral.api.domain.history.entity.ViewingHistory;
import com.vidcentral.api.domain.history.repository.ViewingHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViewingHistoryReadService {

	private final ViewingHistoryRepository viewingHistoryRepository;

	public List<ViewingHistory> findRecentViewingHistoriesBy(Long memberId, Pageable pageable) {
		return viewingHistoryRepository.findWithVideoByMemberId(memberId, pageable);
	}
}
