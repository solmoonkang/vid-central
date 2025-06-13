package com.vidcentral.api.application.auth;

import com.vidcentral.api.dto.auth.response.LoginResponse;
import com.vidcentral.api.dto.auth.response.TokenSaveResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationMapper {

	public static LoginResponse toLoginResponse(String accessToken, String refreshToken) {
		return LoginResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	public static TokenSaveResponse toTokenSaveRequest(String refreshToken) {
		return TokenSaveResponse.builder()
			.refreshToken(refreshToken)
			.build();
	}
}
