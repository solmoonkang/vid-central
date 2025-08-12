package com.vidcentral.api.application.history;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.domain.history.entity.ViewingHistory;
import com.vidcentral.api.dto.history.request.UpdateHistoryRequest;
import com.vidcentral.global.auth.model.AuthMember;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ViewingHistoryService {

	private final ViewingHistoryReadService viewingHistoryReadService;
	private final ViewingHistoryWriteService viewingHistoryWriteService;

	@Transactional
	public void updateViewingHistory(Long videoId, AuthMember authMember, UpdateHistoryRequest updateHistoryRequest) {
		final ViewingHistory viewingHistory = viewingHistoryReadService.findViewingHistory(authMember.id(), videoId);
		viewingHistoryWriteService.updateViewingHistory(viewingHistory, updateHistoryRequest);
	}
}
