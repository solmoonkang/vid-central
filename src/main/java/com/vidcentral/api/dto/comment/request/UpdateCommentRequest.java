package com.vidcentral.api.dto.comment.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "댓글 수정 요청 DTO")
public record UpdateCommentRequest(
	@Schema(description = "댓글 내용", example = "updateContent")
	String content
) {
}
