package com.vidcentral.api.application.member.validator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.vidcentral.api.domain.member.repository.MemberRepository;
import com.vidcentral.global.error.exception.BadRequestException;
import com.vidcentral.global.error.exception.ConflictException;
import com.vidcentral.global.error.model.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberValidator {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;

	public void validateEmailNotDuplicated(String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new ConflictException(ErrorCode.EMAIL_DUPLICATED);
		}
	}

	public void validateNicknameDuplicated(String nickname) {
		if (memberRepository.existsByNickname(nickname)) {
			throw new ConflictException(ErrorCode.NICKNAME_DUPLICATED);
		}
	}

	public void validatePasswordMatch(String password, String passwordConfirm) {
		if (!password.equals(passwordConfirm)) {
			throw new BadRequestException(ErrorCode.PASSWORD_MISMATCH);
		}
	}

	public void validateLoginPassword(String rawPassword, String encodedPassword) {
		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new BadRequestException(ErrorCode.PASSWORD_MISMATCH);
		}
	}
}
