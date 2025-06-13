package com.vidcentral.api.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "사용자 로그인 요청")
public record LoginRequest(
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Schema(description = "사용자 이메일", example = "memberEmail@example.com")
	String email,

	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Schema(description = "사용자 비밀번호", example = "memberPassword123")
	String password
) {
}
