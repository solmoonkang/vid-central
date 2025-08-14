package com.vidcentral.api.dto.reaction.request;

import com.vidcentral.api.domain.reaction.model.ReactionType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "비디오 반응 타입 요청 DTO")
public record ToggleReactionRequest(
	@NotNull(message = "비디오 반응 타입을 입력해주세요.")
	@Schema(description = "좋아요 혹은 싫어요", example = "LIKE, DISLIKE")
	ReactionType reactionType
) {
}
