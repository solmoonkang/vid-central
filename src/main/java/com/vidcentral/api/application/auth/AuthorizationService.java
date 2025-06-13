package com.vidcentral.api.application.auth;

import static com.vidcentral.global.common.util.TokenConstant.*;

import org.springframework.stereotype.Service;

import com.vidcentral.api.domain.auth.repository.TokenRepository;
import com.vidcentral.api.dto.auth.response.LoginResponse;
import com.vidcentral.global.auth.JwtProvider;
import com.vidcentral.global.common.util.CookieUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

	private final JwtProvider jwtProvider;
	private final TokenRepository tokenRepository;

	public LoginResponse issueServiceToken(HttpServletResponse httpServletResponse, String email, String nickname) {
		final String accessToken = jwtProvider.generateAccessToken(email, nickname);
		final String refreshToken = jwtProvider.generateRefreshToken(email);
		tokenRepository.saveToken(email, AuthenticationMapper.toTokenSaveRequest(refreshToken));

		httpServletResponse.setHeader(ACCESS_TOKEN_HEADER, accessToken);

		final Cookie refreshTokenCookie = CookieUtils.generateRefreshTokenCookie(REFRESH_TOKEN_COOKIE, refreshToken);
		httpServletResponse.addCookie(refreshTokenCookie);

		return AuthenticationMapper.toLoginResponse(accessToken, refreshToken);
	}
}
