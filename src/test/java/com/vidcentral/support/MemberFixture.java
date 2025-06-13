package com.vidcentral.support;

import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.member.request.UpdateMemberRequest;
import com.vidcentral.api.dto.member.response.MemberInfoResponse;

public class MemberFixture {

	public static Member createMemberEntity() {
		return Member.builder()
			.email("testMember@example.com")
			.password("test123")
			.nickname("testMemberNickname")
			.build();
	}

	public static MemberInfoResponse createMemberInfoResponse() {
		return MemberInfoResponse.builder()
			.nickname("testMemberNickname")
			.introduce("본인 소개에 대한 내용을 작성해주세요.")
			.build();
	}

	public static UpdateMemberRequest createUpdateMemberRequest() {
		return UpdateMemberRequest.builder()
			.nickname("testMemberNickname")
			.introduce("testIntroduce")
			.build();
	}
}
