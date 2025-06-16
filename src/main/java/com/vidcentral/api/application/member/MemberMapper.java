package com.vidcentral.api.application.member;

import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.member.response.MemberInfoResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MemberMapper {

	public static MemberInfoResponse toMemberInfoResponse(Member member) {
		return new MemberInfoResponse(
			member.getNickname(),
			member.getIntroduce());
	}
}
