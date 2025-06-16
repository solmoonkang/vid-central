package com.vidcentral.api.application.auth;

import static com.vidcentral.global.common.util.TokenConstant.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.domain.auth.repository.TokenRepository;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.auth.request.LoginRequest;
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
	private final MemberReadService memberReadService;
	private final TokenRepository tokenRepository;

	@Transactional
	public LoginResponse loginMember(HttpServletResponse httpServletResponse, LoginRequest loginRequest) {
		final Member loginMember = memberReadService.findMember(loginRequest.email());
		memberReadService.validateLoginPasswordMatch(loginRequest.password(), loginMember.getPassword());

		return issueServiceToken(httpServletResponse, loginMember.getEmail(), loginMember.getNickname());
	}

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
