package com.vidcentral.api.application.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.auth.AuthenticationMapper;
import com.vidcentral.api.application.auth.AuthorizationService;
import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.request.LoginRequest;
import com.vidcentral.api.dto.request.SignUpRequest;
import com.vidcentral.api.dto.request.TokenRequest;
import com.vidcentral.api.dto.request.UpdateMemberRequest;
import com.vidcentral.api.dto.response.LoginResponse;
import com.vidcentral.api.dto.response.MemberInfoResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberWriteService memberWriteService;
	private final MemberReadService memberReadService;
	private final AuthorizationService authorizationService;

	@Transactional
	public void signUpMember(SignUpRequest signUpRequest) {
		memberReadService.validateEmailDuplication(signUpRequest.email());
		memberReadService.validateNicknameDuplication(signUpRequest.nickname());
		memberReadService.validatePasswordMatch(signUpRequest.password(), signUpRequest.checkPassword());
		memberWriteService.signUpMember(signUpRequest);
	}

	@Transactional
	public LoginResponse loginMember(LoginRequest loginRequest) {
		final Member loginMember = memberReadService.findMember(loginRequest.email());
		memberReadService.validateLoginPasswordMatch(loginRequest.password(), loginMember.getPassword());

		TokenRequest tokenRequest = authorizationService
			.issueServiceToken(loginMember.getEmail(), loginMember.getNickname());
		loginMember.updateRefreshToken(tokenRequest.refreshToken());

		return AuthenticationMapper.toLoginResponse(tokenRequest.accessToken(), tokenRequest.refreshToken());
	}

	public MemberInfoResponse searchMemberInfo(Long memberId) {
		final Member loginMember = memberReadService.readMember(memberId);
		return MemberMapper.toMemberInfoResponse(loginMember);
	}

	@Transactional
	public void updateMemberInfo(AuthMember authMember, UpdateMemberRequest updateMemberRequest) {
		final Member loginMember = memberReadService.findMember(authMember.email());
		memberReadService.validateNicknameDuplication(updateMemberRequest.nickname());
		memberReadService.validateProfileImageURLExtension(updateMemberRequest.profileImageURL());

		memberWriteService.updateMemberInfo(loginMember, updateMemberRequest);
	}
}
