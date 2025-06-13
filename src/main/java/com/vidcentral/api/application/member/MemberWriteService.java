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

	public void signUpMember(SignUpRequest signUpRequest) {
		memberRepository.save(MemberMapper.toMember(signUpRequest, passwordEncoder.encode(signUpRequest.password())));
	}

	public void changeMemberInfo(Member member, UpdateMemberRequest updateMemberRequest, String newProfileImageURL) {
		member.updateIntroduce(updateMemberRequest.introduce());
		member.updateNickname(updateMemberRequest.nickname());
		member.updateProfileImageURL(newProfileImageURL);
	}
}
