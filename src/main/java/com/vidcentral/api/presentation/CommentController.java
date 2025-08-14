package com.vidcentral.api.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vidcentral.api.application.comment.CommentService;
import com.vidcentral.api.dto.comment.request.CreateCommentRequest;
import com.vidcentral.api.dto.comment.request.UpdateCommentRequest;
import com.vidcentral.global.auth.annotation.Auth;
import com.vidcentral.global.auth.model.AuthMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/videos/{videoId}/comments")
@RequiredArgsConstructor
@Tag(name = "댓글 APIs", description = "댓글 업로드, 수정, 삭제 등 댓글 관련 API")
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "댓글 업로드", description = "사용자 인증 후 댓글을 업로드합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "🎉 댓글 업로드 성공"),
		@ApiResponse(responseCode = "401", description = "🔒 만료되었거나 위조된 토큰"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 회원 혹은 비디오"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<String> saveComment(
		@PathVariable Long videoId,
		@Auth AuthMember authMember,
		@Valid @RequestBody CreateCommentRequest createCommentRequest) {

		commentService.saveComment(videoId, authMember, createCommentRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 성공적으로 댓글을 업로드했습니다.");
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "댓글 수정", description = "사용자 인증 후 댓글을 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "🎉 댓글 수정 성공"),
		@ApiResponse(responseCode = "401", description = "🔒 만료되었거나 위조된 토큰"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 회원 혹은 비디오"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<String> updateComment(
		@PathVariable Long videoId,
		@Auth AuthMember authMember,
		@Valid @RequestBody UpdateCommentRequest updateCommentRequest) {

		commentService.updateComment(videoId, authMember, updateCommentRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 성공적으로 댓글을 수정했습니다.");
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "댓글 삭제", description = "사용자 인증 후 댓글을 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "🎉 댓글 삭제 성공"),
		@ApiResponse(responseCode = "401", description = "🔒 만료되었거나 위조된 토큰"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 회원 혹은 비디오"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<String> deleteComment(@PathVariable Long videoId, @Auth AuthMember authMember) {
		commentService.deleteComment(videoId, authMember);
		return ResponseEntity.ok().body("[✅ SUCCESS] 성공적으로 댓글을 삭제했습니다.");
	}
}
