package com.vidcentral.api.application.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.member.request.SignUpRequest;
import com.vidcentral.api.dto.member.request.UpdateMemberRequest;
import com.vidcentral.api.dto.member.response.MemberInfoResponse;
import com.vidcentral.global.common.util.HashUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberWriteService memberWriteService;
	private final MemberReadService memberReadService;

	@Transactional
	public void signUpMember(SignUpRequest signUpRequest) {
		memberReadService.validateEmailDuplication(signUpRequest.email());
		memberReadService.validateNicknameDuplication(signUpRequest.nickname());
		memberReadService.validatePasswordMatch(signUpRequest.password(), signUpRequest.checkPassword());

		final String hashResidentNumber = HashUtil.SHA256Hex(signUpRequest.residentNumber());
		memberReadService.validateResidentNumberDuplication(hashResidentNumber);

		memberWriteService.saveMember(signUpRequest, hashResidentNumber);
	}

	// TODO: memberId만 받아서 뭘 어떻게 할건데 이런 식으로 구현을 했는지 이해가 되지 않는다. AuthMember로 검증을 해서 사용자 본인의 정보를 조회할 수 있도록 수정해야 한다.
	public MemberInfoResponse searchMemberInfo(Long memberId) {
		final Member loginMember = memberReadService.readMember(memberId);
		return MemberMapper.toMemberInfoResponse(loginMember);
	}

	@Transactional
	public void updateMemberInfo(AuthMember authMember, UpdateMemberRequest updateMemberRequest) {
		final Member loginMember = memberReadService.findMember(authMember.email());
		memberReadService.validateNicknameDuplication(updateMemberRequest.nickname());
		memberWriteService.changeMemberInfo(loginMember, updateMemberRequest);
	}
}
