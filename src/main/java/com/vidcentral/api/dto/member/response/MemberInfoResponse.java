package com.vidcentral.api.dto.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "사용자 정보 응답")
public record MemberInfoResponse(
	@Schema(description = "사용자 닉네임", example = "memberNickname")
	String nickname,

	@Schema(description = "사용자 소개글", example = "memberIntroduce")
	String introduce
) {
}
