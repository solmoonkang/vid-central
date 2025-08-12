package com.vidcentral.api.application.history;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.history.mapper.ViewingHistoryMapper;
import com.vidcentral.api.domain.history.entity.ViewingHistory;
import com.vidcentral.api.dto.history.request.UpdateHistoryRequest;
import com.vidcentral.api.dto.history.response.ViewingHistoryResponse;
import com.vidcentral.api.dto.pagination.PagedResponse;
import com.vidcentral.api.dto.pagination.PaginationInfo;
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

	public PagedResponse<ViewingHistoryResponse> getViewingHistories(AuthMember authMember, Pageable pageable) {
		if (authMember == null) {
			return new PagedResponse<>(List.of(), new PaginationInfo(0, false));
		}

		return getLoggedInUserHistories(authMember.id(), pageable);
	}

	private PagedResponse<ViewingHistoryResponse> getLoggedInUserHistories(Long memberId, Pageable pageable) {
		final Page<ViewingHistory> viewingHistories = viewingHistoryReadService.findPagedViewingHistoriesBy(
			memberId, pageable);

		final Page<ViewingHistoryResponse> viewingHistoryResponses = viewingHistories
			.map(ViewingHistoryMapper::toViewingHistoryResponse);

		final PaginationInfo paginationInfo = new PaginationInfo(
			viewingHistoryResponses.getNumber(), viewingHistoryResponses.hasNext());
		return new PagedResponse<>(viewingHistoryResponses.getContent(), paginationInfo);
	}
}
