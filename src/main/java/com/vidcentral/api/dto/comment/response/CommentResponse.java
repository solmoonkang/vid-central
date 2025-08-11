package com.vidcentral.api.dto.comment.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "댓글 정보 응답 DTO")
public record CommentResponse(
	@Schema(description = "댓글 업로더 닉네임", example = "memberNickname")
	String authorNickname,

	@Schema(description = "댓글 내용", example = "commentContent")
	String content
) {
}
