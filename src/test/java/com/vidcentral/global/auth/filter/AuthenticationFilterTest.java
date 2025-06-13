package com.vidcentral.global.auth.filter;

import static com.vidcentral.global.common.util.TokenConstant.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.vidcentral.global.auth.JwtProvider;
import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.global.error.exception.NotFoundException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class AuthenticationFilterTest {

	@InjectMocks
	private AuthenticationFilter authenticationFilter;

	@Mock
	private JwtProvider jwtProvider;

	@Mock
	private HandlerExceptionResolver handlerExceptionResolver;

	private HttpServletRequest httpServletRequest;
	private HttpServletResponse httpServletResponse;
	private FilterChain filterChain;

	@BeforeEach
	void setUp() {
		authenticationFilter = new AuthenticationFilter(jwtProvider, handlerExceptionResolver);

		httpServletRequest = mock(HttpServletRequest.class);
		httpServletResponse = mock(HttpServletResponse.class);
		filterChain = mock(FilterChain.class);
	}

	@DisplayName("[✅ SUCCESS] doFilterInternal: 성공적으로 액세스 토큰이 유효한 토큰으로써 인증 및 인가 필터를 통과해서 동작했습니다.")
	@Test
	void doFilterInternal_void_usableAccessToken_success() {
		// GIVEN
		String accessToken = "testAccessToken";
		AuthMember authMember = AuthMember.createAuthMember("testMember@example.com", "testMemberNickname");

		given(jwtProvider.extractToken(eq(ACCESS_TOKEN_HEADER), any(HttpServletRequest.class)))
			.willReturn(accessToken);
		given(jwtProvider.isUsable(accessToken)).willReturn(true);
		given(jwtProvider.extractAuthMemberByAccessToken(accessToken)).willReturn(authMember);

		// WHEN
		authenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
		Authentication actualAuthentication = SecurityContextHolder.getContext().getAuthentication();

		// THEN
		verify(jwtProvider, times(1)).isUsable(accessToken);
		verify(jwtProvider, times(1)).extractAuthMemberByAccessToken(accessToken);

		assertThat(actualAuthentication.getPrincipal()).isEqualTo(authMember);
	}

	// TODO: actualAuthentication이 null이므로, 테스트 코드 동작에 실패했다.
	@DisplayName("[✅ SUCCESS] doFilterInternal: 성공적으로 리프레시 토큰이 유효한 토큰으로써 인증 및 인가 필터를 통과해서 동작했습니다.")
	@Test
	void doFilterInternal_void_usableRefreshToken_success() {
		// GIVEN
		String accessToken = "testAccessToken";
		String newAccessToken = "testNewAccessToken";
		String refreshToken = "testRefreshToken";
		AuthMember authMember = AuthMember.createAuthMember("testMember@example.com", "testMemberNickname");

		MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		FilterChain mockFilterChain = new MockFilterChain();

		given(jwtProvider.extractToken(eq(ACCESS_TOKEN_HEADER), any(HttpServletRequest.class)))
			.willReturn(accessToken);
		given(jwtProvider.extractToken(eq(REFRESH_TOKEN_COOKIE), any(HttpServletRequest.class)))
			.willReturn(refreshToken);
		given(jwtProvider.isUsable(accessToken)).willReturn(false);
		given(jwtProvider.isUsable(refreshToken)).willReturn(true);
		given(jwtProvider.reGenerateToken(refreshToken, mockHttpServletResponse)).willReturn(newAccessToken);
		given(jwtProvider.extractAuthMemberByAccessToken(newAccessToken)).willReturn(authMember);

		// WHEN
		authenticationFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
		Authentication actualAuthentication = SecurityContextHolder.getContext().getAuthentication();

		// THEN
		verify(jwtProvider, times(1)).isUsable(accessToken);

		assertThat(actualAuthentication.getPrincipal()).isEqualTo(authMember);
	}

	@DisplayName("[❎ FAILURE] doFilterInternal: 액세스 토큰과 리프레시 토큰 모두 만료된 토큰입니다.")
	@Test
	void doFilterInternal_expired_NotFoundException_failure() {
		// GIVEN
		String expiredToken = "expiredAccessOrRefreshToken";

		given(jwtProvider.extractToken(any(String.class), any(HttpServletRequest.class)))
			.willReturn(expiredToken);
		given(jwtProvider.isUsable(any(String.class)))
			.willReturn(false);

		// WHEN
		authenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

		// THEN
		verify(handlerExceptionResolver)
			.resolveException(eq(httpServletRequest), eq(httpServletResponse), isNull(), any(NotFoundException.class));
	}
}
