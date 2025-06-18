package com.vidcentral.api.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vidcentral.api.application.media.MediaLikeService;
import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.dto.media.request.ReactionRequest;
import com.vidcentral.global.auth.annotation.Auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/media/{mediaId}/reactions")
@RequiredArgsConstructor
@Tag(name = "반응 APIs", description = "반응 관리 및 반응 개수 조회 API")
public class MediaLikeController {

	private final MediaLikeService mediaLikeService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "좋아요/싫어요 반응",
		description = "로그인된 사용자는 좋아요 또는 싫어요로 반응할 수 있습니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "✅ 좋아요 또는 싫어요 성공"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자 또는 미디어 오류"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류"),
	})
	public ResponseEntity<String> toggleReaction(
		@Auth AuthMember authMember,
		@PathVariable Long mediaId,
		@Valid @RequestBody ReactionRequest reactionRequest) {

		mediaLikeService.toggleReaction(authMember, mediaId, reactionRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 좋아요 또는 싫어요가 성공적으로 반영되었습니다.");
	}
}
