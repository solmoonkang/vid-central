package com.vidcentral.fixtures.auth;

import static com.vidcentral.fixtures.FixtureConstant.*;

import com.vidcentral.global.auth.model.AuthMember;

public class AuthMemberBuilder {

	private Long id = DEFAULT_ID;
	private String email = DEFAULT_EMAIL;
	private String nickname = DEFAULT_NICKNAME;

	public AuthMemberBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public AuthMemberBuilder email(String email) {
		this.email = email;
		return this;
	}

	public AuthMemberBuilder nickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public AuthMember build() {
		return new AuthMember(id, email, nickname);
	}
}
