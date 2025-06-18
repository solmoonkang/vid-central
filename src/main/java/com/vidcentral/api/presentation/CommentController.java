package com.vidcentral.api.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vidcentral.api.application.comment.CommentService;
import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.dto.comment.request.CreateCommentRequest;
import com.vidcentral.api.dto.comment.request.UpdateCommentRequest;
import com.vidcentral.global.auth.annotation.Auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "댓글 APIs", description = "댓글 생성 및 조회 기능을 제공합니다.")
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/{mediaId}")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "댓글 생성",
		description = "로그인된 사용자는 댓글을 생성할 수 있습니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "✅ 댓글 생성 성공"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자 또는 미디어 오류"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류"),
	})
	public ResponseEntity<String> createComment(
		@Auth AuthMember authMember,
		@PathVariable Long mediaId,
		@Valid @RequestBody CreateCommentRequest createCommentRequest) {

		commentService.createComment(authMember, mediaId, createCommentRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 댓글이 성공적으로 생성되었습니다.");
	}

	@PutMapping("/{commentId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "댓글 수정",
		description = "로그인된 사용자는 댓글을 수정할 수 있습니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "✅ 댓글 수정 성공"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "403", description = "⛔️ 접근 권한 오류"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자 또는 미디어 오류"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류"),
	})
	public ResponseEntity<String> updateComment(
		@Auth AuthMember authMember,
		@PathVariable Long commentId,
		@RequestBody UpdateCommentRequest updateCommentRequest) {

		commentService.updateComment(authMember, commentId, updateCommentRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 댓글이 성공적으로 수정되었습니다.");
	}
}
