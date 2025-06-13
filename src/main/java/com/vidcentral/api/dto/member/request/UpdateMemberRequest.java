package com.vidcentral.api.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "사용자 정보 수정 요청")
public record UpdateMemberRequest(
	@NotBlank(message = "닉네임은 필수 입력 항목입니다.")
	@Size(min = 3, max = 20, message = "닉네임은 3자 이상 20자 이하로 설정해야 합니다.")
	String nickname,

	@Size(max = 50, message = "소개글은 최대 50자까지 입력이 가능합니다.")
	String introduce
) {
}
