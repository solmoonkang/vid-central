package com.vidcentral.api.application.member;

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

import com.vidcentral.api.application.member.validator.MemberValidator;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.member.request.SignUpRequest;
import com.vidcentral.api.dto.member.request.UpdateMemberRequest;
import com.vidcentral.fixtures.auth.AuthFixture;
import com.vidcentral.fixtures.member.MemberFixture;
import com.vidcentral.global.auth.model.AuthMember;
import com.vidcentral.global.error.exception.BadRequestException;
import com.vidcentral.global.error.exception.ConflictException;
import com.vidcentral.global.error.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@Mock
	private MemberValidator memberValidator;

	@Mock
	private MemberWriteService memberWriteService;

	@Mock
	private MemberReadService memberReadService;

	@InjectMocks
	private MemberService memberService;

	@Nested
	@DisplayName("사용자 회원가입 테스트: signUpMember")
	class SignUpMemberTest {

		@Nested
		@DisplayName("사용자 회원가입 성공 테스트")
		class SignUpMemberSuccess {
			@Test
			@DisplayName("[✅ SUCCESS] signUpMember: 사용자가 성공적으로 회원가입을 완료했습니다.")
			void signUpMember_success() {
				// GIVEN
				final SignUpRequest signUpRequest = MemberFixture.createSignUpRequest().build();

				doNothing().when(memberValidator).validateEmailNotDuplicated(anyString());
				doNothing().when(memberValidator).validateNicknameDuplicated(anyString());
				doNothing().when(memberValidator).validatePasswordMatch(anyString(), anyString());
				doNothing().when(memberWriteService).saveMember(any(SignUpRequest.class));

				// WHEN
				memberService.signUpMember(signUpRequest);

				// THEN
				verify(memberValidator, times(1)).validateEmailNotDuplicated(signUpRequest.email());
				verify(memberValidator, times(1)).validateNicknameDuplicated(signUpRequest.nickname());
				verify(memberValidator, times(1)).validatePasswordMatch(
					signUpRequest.password(), signUpRequest.checkPassword());
				verify(memberWriteService, times(1)).saveMember(signUpRequest);
			}
		}

		@Nested
		@DisplayName("사용자 회원가입 실패 테스트")
		class SignUpMemberFailure {
			@Test
			@DisplayName("[❎ FAILURE] signUpMember: 이미 존재하는 이메일 요청으로 회원가입에 실패했습니다.")
			void signUpMember_conflictException_emailDuplicated_fail() {
				// GIVEN
				final SignUpRequest duplicateEmailRequest = MemberFixture.createSignUpRequest()
					.email("duplicateTestMember@example.com")
					.build();

				doThrow(new ConflictException(EMAIL_DUPLICATED)).when(memberValidator)
					.validateEmailNotDuplicated(anyString());

				// WHEN & THEN
				assertThatThrownBy(() -> memberService.signUpMember(duplicateEmailRequest))
					.isInstanceOf(ConflictException.class)
					.hasMessageContaining(EMAIL_DUPLICATED.getMessage());
			}

			@Test
			@DisplayName("[❎ FAILURE] signUpMember: 이미 존재하는 닉네임 요청으로 회원가입에 실패했습니다.")
			void signUpMember_conflictException_nicknameDuplicated_fail() {
				// GIVEN
				final SignUpRequest duplicateNicknameRequest = MemberFixture.createSignUpRequest()
					.nickname("duplicateTestMember")
					.build();

				doThrow(new ConflictException(NICKNAME_DUPLICATED)).when(memberValidator)
					.validateNicknameDuplicated(anyString());

				// WHEN & THEN
				assertThatThrownBy(() -> memberService.signUpMember(duplicateNicknameRequest))
					.isInstanceOf(ConflictException.class)
					.hasMessageContaining(NICKNAME_DUPLICATED.getMessage());
			}

			@Test
			@DisplayName("[❎ FAILURE] signUpMember: 입력 비밀번호와 확인 비밀번호가 일치하지 않아 회원가입에 실패했습니다.")
			void signUpMember_badRequestException_passwordMisMatch_fail() {
				// GIVEN
				final SignUpRequest mismatchPasswordRequest = MemberFixture.createSignUpRequest()
					.checkPassword("mismatchTestPassword")
					.build();

				doThrow(new BadRequestException(PASSWORD_MISMATCH)).when(memberValidator)
					.validatePasswordMatch(anyString(), anyString());

				// WHEN & THEN
				assertThatThrownBy(() -> memberService.signUpMember(mismatchPasswordRequest))
					.isInstanceOf(BadRequestException.class)
					.hasMessageContaining(PASSWORD_MISMATCH.getMessage());
			}
		}
	}

	@Nested
	@DisplayName("사용자 정보 수정 테스트: updateMember")
	class UpdateMember {

		@Nested
		@DisplayName("사용자 회원가입 성공 테스트")
		class UpdateMemberSuccess {
			@Test
			@DisplayName("[✅ SUCCESS] updateMember: 사용자가 성공적으로 정보 수정을 완료했습니다.")
			void success_updateMember() {
				// GIVEN
				final AuthMember authMember = AuthFixture.createAuthMember().build();
				final UpdateMemberRequest updateMemberRequest = MemberFixture.createUpdateMemberRequest().build();
				final Member member = MemberFixture.createMemberEntity();

				when(memberReadService.findMemberByEmail(anyString())).thenReturn(member);
				doNothing().when(memberValidator).validateNicknameDuplicated(anyString());
				doNothing().when(memberWriteService)
					.changeMemberInfo(any(Member.class), any(UpdateMemberRequest.class));

				// WHEN
				memberService.updateMember(authMember, updateMemberRequest);

				// THEN
				verify(memberReadService, times(1)).findMemberByEmail(authMember.email());
				verify(memberValidator, times(1)).validateNicknameDuplicated(updateMemberRequest.nickname());
				verify(memberWriteService, times(1)).changeMemberInfo(member, updateMemberRequest);
			}
		}

		@Nested
		@DisplayName("사용자 회원가입 실패 테스트")
		class UpdateMemberFailure {
			@Test
			@DisplayName("[❎ FAILURE] updateMember: 존재하지 않는 사용자의 요청으로 정보 수정에 실패했습니다.")
			void updateMember_notFoundException_fail() {
				// GIVEN
				final AuthMember notExistedAuthMember = AuthFixture.createAuthMember().build();
				final UpdateMemberRequest updateMemberRequest = MemberFixture.createUpdateMemberRequest().build();

				when(memberReadService.findMemberByEmail(anyString()))
					.thenThrow(new NotFoundException(MEMBER_NOT_FOUND));

				// WHEN & THEN
				assertThatThrownBy(() -> memberService.updateMember(notExistedAuthMember, updateMemberRequest))
					.isInstanceOf(NotFoundException.class)
					.hasMessageContaining(MEMBER_NOT_FOUND.getMessage());
			}

			@Test
			@DisplayName("[❎ FAILURE] updateMember: 이미 존재하는 닉네임 요청으로 정보 수정에 실패했습니다.")
			void updateMember_conflictException_nicknameDuplicated_fail() {
				// GIVEN
				final AuthMember authMember = AuthFixture.createAuthMember().build();
				final UpdateMemberRequest duplicateNicknameRequest = MemberFixture.createUpdateMemberRequest()
					.nickname("duplicateTestMember")
					.build();
				final Member member = MemberFixture.createMemberEntity();

				when(memberReadService.findMemberByEmail(anyString())).thenReturn(member);
				doThrow(new ConflictException(NICKNAME_DUPLICATED)).when(memberValidator)
					.validateNicknameDuplicated(anyString());

				// WHEN & THEN
				assertThatThrownBy(() -> memberService.updateMember(authMember, duplicateNicknameRequest))
					.isInstanceOf(ConflictException.class)
					.hasMessageContaining(NICKNAME_DUPLICATED.getMessage());

				verify(memberWriteService, never()).changeMemberInfo(any(Member.class), any(UpdateMemberRequest.class));
			}
		}
	}
}
