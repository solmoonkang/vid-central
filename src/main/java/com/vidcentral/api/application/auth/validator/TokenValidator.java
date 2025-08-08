package com.vidcentral.api.application.auth.validator;

import org.springframework.stereotype.Component;

import com.vidcentral.global.auth.provider.JwtProvider;
import com.vidcentral.global.error.exception.BadRequestException;
import com.vidcentral.global.error.exception.UnauthorizedException;
import com.vidcentral.global.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenValidator {

	private final JwtProvider jwtProvider;

	public void validateTokenFormat(String token) {
		if (!jwtProvider.validateToken(token)) {
			throw new BadRequestException(ErrorMessage.INVALID_TOKEN);
		}
	}

	public void validateTokenExistence(String storedToken, String token) {
		if (!storedToken.equals(token)) {
			throw new UnauthorizedException(ErrorMessage.TOKEN_EXPIRED);
		}
	}
}
