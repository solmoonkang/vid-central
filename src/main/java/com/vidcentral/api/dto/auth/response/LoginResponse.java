package com.vidcentral.api.dto.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "사용자 로그인 응답")
public record LoginResponse(
	@Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR...")
	String accessToken,

	@Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR...")
	String refreshToken
) {
}
