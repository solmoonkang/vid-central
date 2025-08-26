package com.vidcentral.fixtures.member;

import static com.vidcentral.fixtures.FixtureConstant.*;

import com.vidcentral.api.dto.member.request.SignUpRequest;

public class SignUpRequestBuilder {

	private String email = DEFAULT_EMAIL;
	private String password = DEFAULT_PASSWORD;
	private String checkPassword = DEFAULT_PASSWORD;
	private String nickname = DEFAULT_NICKNAME;

	public SignUpRequestBuilder email(String email) {
		this.email = email;
		return this;
	}

	public SignUpRequestBuilder password(String password) {
		this.password = password;
		return this;
	}

	public SignUpRequestBuilder checkPassword(String checkPassword) {
		this.checkPassword = checkPassword;
		return this;
	}

	public SignUpRequestBuilder nickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public SignUpRequest build() {
		return new SignUpRequest(email, password, checkPassword, nickname);
	}
}
