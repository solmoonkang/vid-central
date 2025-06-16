package com.vidcentral.api.application.member;

import static com.vidcentral.global.error.model.ErrorMessage.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.member.repository.MemberRepository;
import com.vidcentral.global.error.exception.BadRequestException;
import com.vidcentral.global.error.exception.ConflictException;
import com.vidcentral.global.error.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberReadService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;

	public Member readMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(FAILED_MEMBER_NOT_FOUND_ERROR));
	}

	public Member findMember(String email) {
		return memberRepository.findMemberByEmail(email)
			.orElseThrow(() -> new NotFoundException(FAILED_MEMBER_NOT_FOUND_ERROR));
	}

	public void validateEmailDuplication(String email) {
		if (memberRepository.existsMemberByEmail(email)) {
			throw new ConflictException(FAILED_EMAIL_DUPLICATION_ERROR);
		}
	}

	public void validateNicknameDuplication(String nickname) {
		if (memberRepository.existsMemberByNickname(nickname)) {
			throw new ConflictException(FAILED_NICKNAME_DUPLICATION_ERROR);
		}
	}

	public void validateResidentNumberDuplication(String residentNumber) {
		if (memberRepository.existsMemberByResidentNumber(residentNumber)) {
			throw new ConflictException(FAILED_RESIDENT_NUMBER_DUPLICATION_ERROR);
		}
	}

	public void validatePasswordMatch(String password, String checkPassword) {
		if (!password.equals(checkPassword)) {
			throw new ConflictException(FAILED_PASSWORD_MISMATCH_ERROR);
		}
	}

	public void validateLoginPasswordMatch(String rawPassword, String encodedPassword) {
		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new BadRequestException(FAILED_INVALID_PASSWORD_ERROR);
		}
	}
}
