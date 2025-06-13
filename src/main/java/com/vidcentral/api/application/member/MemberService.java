package com.vidcentral.api.application.member;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.vidcentral.api.application.auth.AuthorizationService;
import com.vidcentral.api.application.media.MediaService;
import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.auth.request.LoginRequest;
import com.vidcentral.api.dto.member.request.SignUpRequest;
import com.vidcentral.api.dto.member.request.UpdateMemberRequest;
import com.vidcentral.api.dto.auth.response.LoginResponse;
import com.vidcentral.api.dto.member.response.MemberInfoResponse;

import jakarta.servlet.http.HttpServletResponse;
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
	public LoginResponse loginMember(HttpServletResponse httpServletResponse, LoginRequest loginRequest) {
		final Member loginMember = memberReadService.findMember(loginRequest.email());
		memberReadService.validateLoginPasswordMatch(loginRequest.password(), loginMember.getPassword());

		return authorizationService
			.issueServiceToken(httpServletResponse, loginMember.getEmail(), loginMember.getNickname());
	}

	public MemberInfoResponse searchMemberInfo(Long memberId) {
		final Member loginMember = memberReadService.readMember(memberId);
		return MemberMapper.toMemberInfoResponse(loginMember);
	}

	@Transactional
	public void updateMemberInfo(AuthMember authMember, UpdateMemberRequest updateMemberRequest,
		MultipartFile profileImageURL) {

		final Member loginMember = memberReadService.findMember(authMember.email());
		memberReadService.validateNicknameDuplication(updateMemberRequest.nickname());

		String newProfileImageURL = mediaService.uploadImages(List.of(profileImageURL)).get(0);
		memberWriteService.changeMemberInfo(loginMember, updateMemberRequest, newProfileImageURL);
	}
}
