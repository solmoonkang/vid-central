package com.vidcentral.fixtures.auth;

import static com.vidcentral.fixtures.FixtureConstant.*;

import com.vidcentral.api.dto.auth.request.LoginRequest;

public class LoginRequestBuilder {

	private String email = DEFAULT_EMAIL;
	private String password = DEFAULT_PASSWORD;

	public LoginRequestBuilder email(String email) {
		this.email = email;
		return this;
	}

	public LoginRequestBuilder password(String password) {
		this.password = password;
		return this;
	}

	public LoginRequest build() {
		return new LoginRequest(email, password);
	}
}
