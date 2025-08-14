package com.vidcentral.api.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vidcentral.api.application.reaction.VideoReactionService;
import com.vidcentral.api.dto.reaction.request.ToggleReactionRequest;
import com.vidcentral.global.auth.annotation.Auth;
import com.vidcentral.global.auth.model.AuthMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/videos/{videoId}/reactions")
@RequiredArgsConstructor
@Tag(name = "비디오 반응 토글 APIs", description = "특정 비디오 반응 등록, 취소, 변경 등 관련 API")
public class VideoReactionController {

	private final VideoReactionService videoReactionService;

	@PatchMapping
	@Operation(summary = "비디오 반응 토글", description = "특정 비디오에 좋아요 또는 싫어요 반응을 등록/취소/변경합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "🎉 비디오 반응 적용 성공"),
		@ApiResponse(responseCode = "401", description = "🔒 만료되었거나 위조된 토큰"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 비디오"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<String> toggleVideoReaction(
		@PathVariable Long videoId,
		@Auth AuthMember authMember,
		@Valid @RequestBody ToggleReactionRequest toggleReactionRequest) {

		videoReactionService.toggleVideoReaction(videoId, authMember, toggleReactionRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 성공적으로 비디오에 좋아요 또는 싫어요 반응을 등록했습니다.");
	}
}
