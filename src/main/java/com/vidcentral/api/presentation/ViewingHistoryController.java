package com.vidcentral.api.presentation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vidcentral.api.application.history.ViewingHistoryService;
import com.vidcentral.api.dto.history.request.UpdateHistoryRequest;
import com.vidcentral.api.dto.history.response.ViewingHistoryResponse;
import com.vidcentral.api.dto.pagination.PagedResponse;
import com.vidcentral.global.auth.annotation.Auth;
import com.vidcentral.global.auth.model.AuthMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/viewingHistories")
@RequiredArgsConstructor
@Tag(name = "시청 기록 APIs", description = "시청 기록 수정 및 시청 기록 목록 조회 등 시청 기록 관련 API")
public class ViewingHistoryController {

	private final ViewingHistoryService viewingHistoryService;

	@PutMapping("/{videoId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "시청 기록 수정", description = "인증된 사용자의 시청 기록을 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "🎉 시청 기록 수정 성공"),
		@ApiResponse(responseCode = "401", description = "🔒 만료되었거나 위조된 토큰"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 시청 기록"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<String> updateViewingHistory(
		@PathVariable Long videoId,
		@Auth AuthMember authMember,
		@Valid @RequestBody UpdateHistoryRequest updateHistoryRequest) {

		viewingHistoryService.updateViewingHistory(videoId, authMember, updateHistoryRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 성공적으로 시청 기록을 수정했습니다.");
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "시청 기록 목록 조회", description = "로그인한 사용자의 시청 기록 목록을 조회합니다. 비로그인 사용자는 빈 목록을 반환합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "🎉 시청 기록 목록 조회 성공"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<PagedResponse<ViewingHistoryResponse>> getViewingHistories(
		@Auth AuthMember authMember,
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

		return ResponseEntity.ok().body(viewingHistoryService.getViewingHistories(authMember, pageable));
	}
}
