package com.vidcentral.api.domain.auth.entity;

import java.util.Objects;

public record AuthMember(
	String email,

	String nickname
) {

	public static AuthMember createAuthMember(String email, String nickname) {
		return new AuthMember(Objects.requireNonNull(email), Objects.requireNonNull(nickname));
	}
}
