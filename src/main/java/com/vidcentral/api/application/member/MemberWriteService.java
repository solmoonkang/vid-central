package com.vidcentral.api.application.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.member.repository.MemberRepository;
import com.vidcentral.api.dto.member.request.SignUpRequest;
import com.vidcentral.api.dto.member.request.UpdateMemberRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberWriteService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public void saveMember(SignUpRequest signUpRequest) {
		final String encodedPassword = passwordEncoder.encode(signUpRequest.password());
		final Member member = Member.createMember(signUpRequest, encodedPassword);
		memberRepository.save(member);
	}

	public void changeMemberInfo(Member member, UpdateMemberRequest updateMemberRequest) {
		member.updateIntroduce(updateMemberRequest.introduce());
		member.updateNickname(updateMemberRequest.nickname());
	}
}
