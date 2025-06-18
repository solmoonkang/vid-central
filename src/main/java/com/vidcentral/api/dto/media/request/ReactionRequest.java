package com.vidcentral.api.dto.media.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "좋아요/싫어요 요청 DTO")
public record ReactionRequest(
	@Schema(description = "좋아요/싫어요", example = "true/false")
	@NotNull(message = "좋아요 또는 싫어요를 눌러주세요.")
	boolean isLike
) {
}
