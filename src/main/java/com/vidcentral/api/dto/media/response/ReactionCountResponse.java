package com.vidcentral.api.dto.media.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "좋아요/싫어요 요청 개수 응답 DTO")
public record ReactionCountResponse(
	@Schema(description = "좋아요 요청 개수", example = "999")
	long likeCount,

	@Schema(description = "싫어요 요청 개수", example = "999")
	long dislikeCount
) {
}
