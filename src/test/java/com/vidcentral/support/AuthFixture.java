package com.vidcentral.support;

import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.auth.request.LoginRequest;
import com.vidcentral.api.dto.member.request.SignUpRequest;
import com.vidcentral.api.dto.auth.response.LoginResponse;

public class AuthFixture {

	public static SignUpRequest createSignUpRequest() {
		return SignUpRequest.builder()
			.email("testMember@example.com")
			.password("test123")
			.checkPassword("test123")
			.nickname("testMemberNickname")
			.build();
	}

	public static LoginRequest createLoginRequest() {
		return LoginRequest.builder()
			.email("testMember@example.com")
			.password("test123")
			.build();
	}

	public static LoginRequest createLoginRequest(Member member) {
		return new LoginRequest(member.getEmail(), member.getPassword());
	}

	public static LoginResponse createLoginResponse() {
		return LoginResponse.builder()
			.accessToken("testAccessToken")
			.refreshToken("testRefreshToken")
			.build();
	}
}
