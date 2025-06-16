package com.vidcentral.api.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "사용자 회원가입 요청")
public record SignUpRequest(
	@Email(message = "유효한 이메일 형식을 입력해주세요.")
	@Schema(description = "사용자 이메일", example = "memberEmail@example.com")
	String email,

	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Size(min = 5, max = 20, message = "비밀번호는 5자 이상 20자 이하로 설정해야 합니다.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,20}$", message = "비밀번호는 영문자와 숫자를 모두 포함해야 합니다.")
	@Schema(description = "사용자 비밀번호", example = "memberPassword123")
	String password,

	@NotBlank(message = "비밀번호 확인은 필수 입력 항목입니다.")
	@Schema(description = "사용자 비밀번호 확인", example = "memberPassword123")
	String checkPassword,

	@NotBlank(message = "닉네임은 필수 입력 항목입니다.")
	@Size(min = 3, max = 20, message = "닉네임은 3자 이상 20자 이하로 설정해야 합니다.")
	@Schema(description = "사용자 닉네임", example = "memberNickname")
	String nickname,

	@Schema(description = "사용자 주민등록번호", example = "111111-1234567")
	@NotBlank(message = "사용자 주민등록번호를 입력해주세요.")
	@Pattern(
		regexp = "^\\d{6}-[1-4]\\d{6}$",
		message = "주민등록번호 형식에 맞게 입력해주세요. (예: 111111-1234567)"
	)
	String residentNumber
) {
}
