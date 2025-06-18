package com.vidcentral.api.dto.comment.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "댓글 요청 DTO")
public record CreateCommentRequest(
	@Schema(description = "댓글 내용", example = "commentContent")
	@NotBlank(message = "댓글 내용을 입력해주세요.")
	String content
) {
}
