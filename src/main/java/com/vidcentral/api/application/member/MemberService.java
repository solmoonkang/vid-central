package com.vidcentral.api.application.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.member.validator.MemberValidator;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.member.request.SignUpRequest;
import com.vidcentral.api.dto.member.request.UpdateMemberRequest;
import com.vidcentral.global.auth.model.AuthMember;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberValidator memberValidator;
	private final MemberWriteService memberWriteService;
	private final MemberReadService memberReadService;

	@Transactional
	public void signUpMember(SignUpRequest signUpRequest) {
		memberValidator.validateEmailNotDuplicated(signUpRequest.email());
		memberValidator.validateNicknameDuplicated(signUpRequest.nickname());
		memberValidator.validatePasswordMatch(signUpRequest.password(), signUpRequest.checkPassword());
		memberWriteService.saveMember(signUpRequest);
	}

	@Transactional
	public void updateMember(AuthMember authMember, UpdateMemberRequest updateMemberRequest) {
		final Member loginMember = memberReadService.getMemberByEmail(authMember.email());
		memberValidator.validateNicknameDuplicated(updateMemberRequest.nickname());
		memberWriteService.changeMemberInfo(loginMember, updateMemberRequest);
	}
}
