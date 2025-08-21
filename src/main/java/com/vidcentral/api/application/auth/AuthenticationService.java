package com.vidcentral.api.application.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.auth.mapper.TokenMapper;
import com.vidcentral.api.application.auth.validator.TokenValidator;
import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.application.member.validator.MemberValidator;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.auth.request.LoginRequest;
import com.vidcentral.api.dto.auth.response.TokenResponse;
import com.vidcentral.global.auth.model.AuthMember;
import com.vidcentral.global.auth.provider.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final JwtProvider jwtProvider;
	private final MemberValidator memberValidator;
	private final TokenValidator tokenValidator;
	private final MemberReadService memberReadService;

	@Transactional
	public TokenResponse loginMember(LoginRequest loginRequest) {
		final Member member = memberReadService.findMemberByEmail(loginRequest.email());
		memberValidator.validateLoginPassword(loginRequest.password(), member.getPassword());

		final String accessToken = generateAccessToken(member);
		final String refreshToken = generateRefreshToken(member);
		member.updateMemberRefreshToken(refreshToken);

		return TokenMapper.createTokenResponse(accessToken, refreshToken);
	}

	@Transactional
	public TokenResponse reissueTokens(String refreshToken) {
		tokenValidator.validateTokenFormat(refreshToken);
		final AuthMember authMember = jwtProvider.extractAuthMemberFromToken(refreshToken);

		final Member member = memberReadService.findMemberByEmail(authMember.email());
		tokenValidator.validateTokenExistence(member.getRefreshToken(), refreshToken);

		final String newAccessToken = generateAccessToken(member);
		final String newRefreshToken = generateRefreshToken(member);
		member.updateMemberRefreshToken(newRefreshToken);

		return TokenMapper.createTokenResponse(newAccessToken, newRefreshToken);
	}

	private String generateAccessToken(Member member) {
		return jwtProvider.generateAccessToken(member.getId(), member.getEmail(), member.getNickname());
	}

	private String generateRefreshToken(Member member) {
		return jwtProvider.generateRefreshToken(member.getEmail());
	}
}
