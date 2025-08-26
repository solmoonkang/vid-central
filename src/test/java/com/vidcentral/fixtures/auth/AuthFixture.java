package com.vidcentral.fixtures.auth;

public class AuthFixture {

	public static AuthMemberBuilder createAuthMember() {
		return new AuthMemberBuilder();
	}

	public static LoginRequestBuilder createLoginRequest() {
		return new LoginRequestBuilder();
	}
}
