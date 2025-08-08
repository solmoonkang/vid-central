package com.vidcentral.api.application.auth.mapper;

import com.vidcentral.api.dto.auth.response.TokenResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenMapper {

	public static TokenResponse createTokenResponse(String accessToken, String refreshToken) {
		return new TokenResponse(accessToken, refreshToken);
	}
}
