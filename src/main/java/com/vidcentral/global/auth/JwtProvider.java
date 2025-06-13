package com.vidcentral.global.auth;

import static com.vidcentral.global.common.util.GlobalConstant.*;
import static com.vidcentral.global.common.util.TokenConstant.*;
import static com.vidcentral.global.error.model.ErrorMessage.*;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.domain.auth.repository.TokenRepository;
import com.vidcentral.api.dto.auth.response.TokenSaveResponse;
import com.vidcentral.global.common.util.CookieUtils;
import com.vidcentral.global.config.TokenConfig;
import com.vidcentral.global.error.exception.NotFoundException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtProvider {

	private final TokenConfig tokenConfig;
	private final TokenRepository tokenRepository;

	public String generateAccessToken(String email, String nickname) {
		final Date issuedDate = new Date();
		final Date expiredDate = new Date(issuedDate.getTime() + tokenConfig.getAccessTokenExpire());

		return buildJwt(issuedDate, expiredDate)
			.claim(MEMBER_EMAIL, email)
			.claim(MEMBER_NICKNAME, nickname)
			.compact();
	}

	public String generateRefreshToken(String email) {
		final Date issuedDate = new Date();
		final Date expiredDate = new Date(issuedDate.getTime() + tokenConfig.getRefreshTokenExpire());

		return buildJwt(issuedDate, expiredDate)
			.claim(MEMBER_EMAIL, email)
			.compact();
	}

	@Transactional
	public String reGenerateToken(String refreshToken, HttpServletResponse httpServletResponse) {
		final Claims claims = parseClaimsByToken(refreshToken);
		final String memberEmail = claims.get(MEMBER_EMAIL, String.class);
		final String memberNickname = claims.get(MEMBER_NICKNAME, String.class);

		TokenSaveResponse tokenSaveResponse = tokenRepository.getTokenSaveValue(memberEmail);
		validateTokenResponse(tokenSaveResponse);

		validateRefreshToken(refreshToken, tokenSaveResponse.refreshToken());

		final String newAccessToken = generateAccessToken(memberEmail, memberNickname);
		final String newRefreshToken = generateRefreshToken(memberEmail);

		httpServletResponse.setHeader(ACCESS_TOKEN_HEADER, newAccessToken);

		final Cookie refreshTokenCookie = CookieUtils.generateRefreshTokenCookie(REFRESH_TOKEN_COOKIE, newRefreshToken);
		httpServletResponse.addCookie(refreshTokenCookie);

		return newAccessToken;
	}

	public String extractToken(String header, HttpServletRequest httpServletRequest) {
		final String token = httpServletRequest.getHeader(header);

		if (token == null || !token.startsWith(BEARER)) {
			log.warn("[✅ LOGGER] {}는 NULL이거나 BEARER가 아닙니다.", header);
			return null;
		}

		return token.replaceFirst(BEARER, BLANK).trim();
	}

	public AuthMember extractAuthMemberByAccessToken(String accessToken) {
		final Claims claims = parseClaimsByToken(accessToken);
		final String memberEmail = claims.get(MEMBER_EMAIL, String.class);
		final String memberNickname = claims.get(MEMBER_NICKNAME, String.class);

		return AuthMember.createAuthMember(memberEmail, memberNickname);
	}

	public boolean isUsable(String token) {
		try {
			Jwts.parser()
				.verifyWith(tokenConfig.getSecretKey())
				.build()
				.parseSignedClaims(token);

			return true;
		} catch (ExpiredJwtException expiredJwtException) {
			log.warn("[✅ LOGGER] JWT 토큰이 만료되었습니다.");
		} catch (IllegalArgumentException illegalArgumentException) {
			log.warn("[✅ LOGGER] JWT 토큰이 존재하지 않습니다.");
			throw new NotFoundException(FAILED_TOKEN_NOT_FOUND_ERROR);
		} catch (Exception exception) {
			log.warn("[✅ LOGGER] 유효하지 않은 토큰입니다.");
			throw new NotFoundException(FAILED_INVALID_TOKEN_ERROR);
		}

		return false;
	}

	private JwtBuilder buildJwt(Date issuedDate, Date expiredDate) {
		return Jwts.builder()
			.issuer(tokenConfig.getIss())
			.issuedAt(issuedDate)
			.expiration(expiredDate)
			.signWith(tokenConfig.getSecretKey());
	}

	private Claims parseClaimsByToken(String token) {
		return Jwts.parser()
			.verifyWith(tokenConfig.getSecretKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	private void validateRefreshToken(String reGenerateRefreshToken, String savedRefreshToken) {
		if (!reGenerateRefreshToken.equals(savedRefreshToken)) {
			log.warn("[✅ LOGGER] 유효하지 않은 리프레시 토큰입니다.");
			throw new NotFoundException(FAILED_TOKEN_NOT_FOUND_ERROR);
		}
	}

	private void validateTokenResponse(TokenSaveResponse tokenSaveResponse) {
		if (tokenSaveResponse == null || tokenSaveResponse.refreshToken() == null) {
			throw new NotFoundException(FAILED_UNAUTHORIZED_MEMBER_ERROR);
		}
	}
}
