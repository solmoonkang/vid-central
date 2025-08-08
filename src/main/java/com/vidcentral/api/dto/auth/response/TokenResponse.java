package com.vidcentral.api.dto.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "토큰 응답 DTO")
public record TokenResponse(
	@Schema(description = "액세스 토큰", example = "ssecxasxsffsf32xss1sf0sa8sd...")
	String accessToken,

	@Schema(description = "리프레시 토큰", example = "ssecxasxsffsf32xss1sf0sa8st...")
	String refreshToken
) {
}
