package com.vidcentral.api.dto.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "토큰 저장 응답")
public record TokenSaveResponse(
	@Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR...")
	String refreshToken
) {
}
