package com.vidcentral.api.application.auth;

import static com.vidcentral.global.common.util.GlobalConstant.*;
import static com.vidcentral.global.common.util.TokenConstant.*;
import static com.vidcentral.global.error.model.ErrorMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;

import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.domain.auth.repository.TokenRepository;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.auth.response.TokenSaveResponse;
import com.vidcentral.global.auth.JwtProvider;
import com.vidcentral.global.config.TokenConfig;
import com.vidcentral.global.error.exception.NotFoundException;
import com.vidcentral.support.JwtFixture;
import com.vidcentral.support.MemberFixture;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

class JwtProviderTest {

	@Mock
	private TokenConfig tokenConfig;

	@Mock
	private TokenRepository tokenRepository;

	@InjectMocks
	private JwtProvider jwtProvider;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		when(tokenConfig.getIss()).thenReturn("test_issuer");

		String base64SecretKey = "c29tZSByYW5kb20gc3RyaW5nIHN0cmluZwAAc29tZSByYW5kb20gc3RyaW5nIHN0cmluZwAA";
		when(tokenConfig.getSecretAccessKey()).thenReturn(base64SecretKey);

		byte[] secretKeyBytes = Decoders.BASE64.decode(base64SecretKey);
		SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
		when(tokenConfig.getSecretKey()).thenReturn(secretKey);

		when(tokenConfig.getAccessTokenExpire()).thenReturn(3600000L);
		when(tokenConfig.getRefreshTokenExpire()).thenReturn(604800000L);
		when(tokenRepository.getTokenSaveValue("testMember@example.com")).thenReturn(
			new TokenSaveResponse("testMemberRefreshToken"));
	}

	@DisplayName("[✅ SUCCESS] generateAccessToken: 성공적으로 액세스 토큰을 발급했습니다.")
	@Test
	void generateAccessToken_accessToken_success() {
		// GIVEN
		String memberEmail = "testMember@example.com";
		String memberNickname = "testMemberNickname";

		// WHEN
		String accessToken = jwtProvider.generateAccessToken(memberEmail, memberNickname);

		// THEN
		Claims actualClaims = Jwts.parser()
			.verifyWith(tokenConfig.getSecretKey())
			.build()
			.parseSignedClaims(accessToken)
			.getPayload();

		assertThat(actualClaims.get(MEMBER_EMAIL, String.class)).isEqualTo(memberEmail);
		assertThat(actualClaims.get(MEMBER_NICKNAME, String.class)).isEqualTo(memberNickname);
	}

	@DisplayName("[✅ SUCCESS] generateRefreshToken: 성공적으로 리프레시 토큰을 발급했습니다.")
	@Test
	void generateRefreshToken_refreshToken_success() {
		// GIVEN
		String memberEmail = "testMember@example.com";

		// WHEN
		String refreshToken = jwtProvider.generateRefreshToken(memberEmail);

		// THEN
		Claims actualClaims = Jwts.parser()
			.verifyWith(tokenConfig.getSecretKey())
			.build()
			.parseSignedClaims(refreshToken)
			.getPayload();

		assertThat(actualClaims.get(MEMBER_EMAIL, String.class)).isEqualTo(memberEmail);
	}

	@DisplayName("[✅ SUCCESS] reGenerateToken: 성공적으로 리프레시 토큰으로 액세스 토큰을 재발급했습니다.")
	@Test
	void reGenerateToken_accessToken_success() {
		// GIVEN
		Member loginMember = MemberFixture.createMemberEntity();
		String refreshToken = jwtProvider.generateRefreshToken(loginMember.getEmail());
		HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

		given(tokenRepository.getTokenSaveValue(any())).willReturn(new TokenSaveResponse(refreshToken));

		// WHEN
		String newAccessToken = jwtProvider.reGenerateToken(refreshToken, httpServletResponse);
		Claims actualClaims = Jwts.parser()
			.verifyWith(tokenConfig.getSecretKey())
			.build()
			.parseSignedClaims(newAccessToken)
			.getPayload();

		// THEN
		assertThat(actualClaims.get(MEMBER_EMAIL, String.class)).isEqualTo(loginMember.getEmail());

		verify(httpServletResponse).setHeader(ACCESS_TOKEN_HEADER, newAccessToken);
		verify(httpServletResponse).addCookie(any(Cookie.class));
	}

	@DisplayName("[❎ FAILURE] reGenerateToken: 리프레시 토큰 정보에 해당하는 사용자가 없습니다.")
	@Test
	void reGenerateToken_member_NotFoundException_failure() {
		// GIVEN
		Member loginMember = MemberFixture.createMemberEntity();
		String refreshToken = jwtProvider.generateRefreshToken(loginMember.getEmail());
		HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

		given(tokenRepository.getTokenSaveValue(any())).willReturn(null);

		// WHEN & THEN
		assertThatThrownBy(() -> jwtProvider.reGenerateToken(refreshToken, httpServletResponse))
			.isInstanceOf(NotFoundException.class)
			.hasMessage(FAILED_UNAUTHORIZED_MEMBER_ERROR.getMessage());
	}

	@DisplayName("[✅ SUCCESS] extractToken: 성공적으로 액세스 토큰을 추출했습니다.")
	@Test
	void extractToken_validToken_success() {
		// GIVEN
		String accessToken = "testAccessToken";

		MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
		mockHttpServletRequest.addHeader(ACCESS_TOKEN_HEADER, BEARER + BLANK + accessToken);

		// WHEN
		String actualToken = jwtProvider.extractToken(ACCESS_TOKEN_HEADER, mockHttpServletRequest);

		// THEN
		assertThat(actualToken).isEqualTo(accessToken);
	}

	@DisplayName("[❎ FAILURE] extractToken: BEARER 타입이 아닌 토큰 추출로 NULL을 반환했습니다.")
	@Test
	void extractToken_null_failure() {
		// GIVEN
		String accessToken = "testAccessToken";

		MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
		mockHttpServletRequest.addHeader(ACCESS_TOKEN_HEADER, accessToken);

		// WHEN
		String actualToken = jwtProvider.extractToken(ACCESS_TOKEN_HEADER, mockHttpServletRequest);

		// THEN
		assertThat(actualToken).isNull();
	}

	@DisplayName("[✅ SUCCESS] extractAuthMemberByAccessToken: 성공적으로 액세스 토큰 정보를 추출해서 AuthMember를 생성했습니다.")
	@Test
	void extractAuthMemberByAccessToken_AuthMember_success() {
		// GIVEN
		String memberEmail = "testMember@example.com";
		String memberNickname = "testMemberNickname";
		String accessToken = jwtProvider.generateAccessToken(memberEmail, memberNickname);

		// WHEN
		AuthMember actualAuthMember = jwtProvider.extractAuthMemberByAccessToken(accessToken);

		// THEN
		assertThat(actualAuthMember.email()).isEqualTo(memberEmail);
		assertThat(actualAuthMember.nickname()).isEqualTo(memberNickname);
	}

	@DisplayName("[✅ SUCCESS] isUsable: 성공적으로 토큰이 유효한지 확인했습니다.")
	@Test
	void isUsable_true_success() {
		// GIVEN
		String memberEmail = "testMember@example.com";
		String memberNickname = "testMemberNickname";
		String accessToken = jwtProvider.generateAccessToken(memberEmail, memberNickname);

		// WHEN
		boolean actualTokenUsable = jwtProvider.isUsable(accessToken);

		// THEN
		assertThat(actualTokenUsable).isTrue();
	}

	@DisplayName("[❎ FAILURE] isUsable: 해당 토큰은 만료된 토큰입니다.")
	@Test
	void isUsable_false_failure() {
		// GIVEN
		String expiredAccessToken = JwtFixture.createExpiredToken(tokenConfig.getSecretKey());

		// WHEN
		boolean actualTokenUsable = jwtProvider.isUsable(expiredAccessToken);

		// THEN
		assertThat(actualTokenUsable).isFalse();
	}

	@DisplayName("[❎ FAILURE] isUsable: 해당 토큰은 비어있는 토큰입니다.")
	@Test
	void isUsable_empty_NotFoundException_failure() {
		// GIVEN
		String emptyAccessToken = "";

		// WHEN & THEN
		assertThatThrownBy(() -> jwtProvider.isUsable(emptyAccessToken))
			.isInstanceOf(NotFoundException.class)
			.hasMessage(FAILED_TOKEN_NOT_FOUND_ERROR.getMessage());
	}

	@DisplayName("[❎ FAILURE] isUsable: 해당 토큰은 잘못된 토큰입니다.")
	@Test
	void isUsable_invalid_NotFoundException_failure() {
		// GIVEN
		String invalidAccessToken = "invalidAccessToken";

		// WHEN & THEN
		assertThatThrownBy(() -> jwtProvider.isUsable(invalidAccessToken))
			.isInstanceOf(NotFoundException.class)
			.hasMessage(FAILED_INVALID_TOKEN_ERROR.getMessage());
	}
}
