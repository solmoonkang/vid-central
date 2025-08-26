package com.vidcentral.api.application.auth;

import static com.vidcentral.global.error.model.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.application.member.validator.MemberValidator;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.auth.request.LoginRequest;
import com.vidcentral.api.dto.auth.response.TokenResponse;
import com.vidcentral.fixtures.auth.AuthFixture;
import com.vidcentral.fixtures.member.MemberFixture;
import com.vidcentral.global.auth.provider.JwtProvider;
import com.vidcentral.global.error.exception.BadRequestException;
import com.vidcentral.global.error.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationService 단위 테스트")
class AuthenticationServiceTest {

	@Mock
	private JwtProvider jwtProvider;

	@Mock
	private MemberValidator memberValidator;

	@Mock
	private MemberReadService memberReadService;

	@InjectMocks
	private AuthenticationService authenticationService;

	@Nested
	@DisplayName("사용자 로그인 테스트: loginMember")
	class LoginMemberTest {

		@Nested
		@DisplayName("사용자 로그인 성공 테스트")
		class LoginMemberSuccess {
			@Test
			@DisplayName("[✅ SUCCESS] loginMember: 사용자가 성공적으로 로그인을 완료했습니다.")
			void loginMember_success() {
				// GIVEN
				final LoginRequest loginRequest = AuthFixture.createLoginRequest().build();
				final Member member = MemberFixture.createMemberEntity();
				final String accessToken = "testAccessToken";
				final String refreshToken = "testRefreshToken";

				when(memberReadService.findMemberByEmail(anyString())).thenReturn(member);
				doNothing().when(memberValidator).validateLoginPassword(anyString(), anyString());
				when(jwtProvider.generateAccessToken(anyLong(), anyString(), anyString())).thenReturn(accessToken);
				when(jwtProvider.generateRefreshToken(anyString())).thenReturn(refreshToken);

				// WHEN
				final TokenResponse tokenResponse = authenticationService.loginMember(loginRequest);

				// THEN
				verify(memberReadService, times(1)).findMemberByEmail(loginRequest.email());
				verify(memberValidator, times(1)).validateLoginPassword(loginRequest.password(), member.getPassword());
				verify(jwtProvider, times(1)).generateAccessToken(member.getId(), member.getEmail(),
					member.getNickname());
				verify(jwtProvider, times(1)).generateRefreshToken(member.getEmail());
				verify(member, times(1)).updateMemberRefreshToken(refreshToken);

				assertThat(tokenResponse.accessToken()).isEqualTo(accessToken);
				assertThat(tokenResponse.refreshToken()).isEqualTo(refreshToken);
			}
		}

		@Nested
		@DisplayName("사용자 로그인 실패 테스트")
		class LoginMemberFailure {
			@Test
			@DisplayName("[❎ FAILURE] loginMember: 존재하지 않는 사용자 요청으로 로그인에 실패했습니다.")
			void loginMember_notFoundException_fail() {
				// GIVEN
				final LoginRequest notExistedLoginRequest = AuthFixture.createLoginRequest()
					.email("notExistTestMember@example.com")
					.build();

				when(memberReadService.findMemberByEmail(anyString()))
					.thenThrow(new NotFoundException(MEMBER_NOT_FOUND));

				// WHEN & THEN
				assertThatThrownBy(() -> authenticationService.loginMember(notExistedLoginRequest))
					.isInstanceOf(NotFoundException.class)
					.hasMessageContaining(MEMBER_NOT_FOUND.getMessage());

				verify(memberValidator, never()).validateLoginPassword(anyString(), anyString());
				verify(jwtProvider, never()).generateAccessToken(anyLong(), anyString(), anyString());
			}

			@Test
			@DisplayName("[❎ FAILURE] loginMember: 입력 비밀번호와 확인 비밀번호가 일치하지 않아 로그인에 실패했습니다.")
			void loginMember_badRequestException_passwordMismatch_fail() {
				// GIVEN
				final LoginRequest mismatchPasswordRequest = AuthFixture.createLoginRequest()
					.password("mismatchTestPassword")
					.build();
				final Member member = MemberFixture.createMemberEntity();

				when(memberReadService.findMemberByEmail(anyString())).thenReturn(member);
				doThrow(new BadRequestException(PASSWORD_MISMATCH)).when(memberValidator)
					.validateLoginPassword(anyString(), anyString());

				// WHEN & THEN
				assertThatThrownBy(() -> authenticationService.loginMember(mismatchPasswordRequest))
					.isInstanceOf(BadRequestException.class)
					.hasMessageContaining(PASSWORD_MISMATCH.getMessage());

				verify(jwtProvider, never()).generateAccessToken(anyLong(), anyString(), anyString());
				verify(jwtProvider, never()).generateRefreshToken(anyString());
				verify(member, never()).updateMemberRefreshToken(anyString());
			}
		}
	}

	@Nested
	@DisplayName("토큰 재발급 테스트: reissueTokens")
	class ReissueTokensTest {

		@Nested
		@DisplayName("토큰 재발급 성공 테스트")
		class ReissueTokensSuccess {
			@Test
			@DisplayName("[✅ SUCCESS] reissueTokens: 성공적으로 토큰 재발급을 완료했습니다.")
			void reissueTokens_success() {
			}
		}

		@Nested
		@DisplayName("토큰 재발급 실패 테스트")
		class ReissueTokensFailure {

		}
	}
}
