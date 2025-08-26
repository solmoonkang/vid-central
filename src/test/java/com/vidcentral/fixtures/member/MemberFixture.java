package com.vidcentral.fixtures.member;

import com.vidcentral.api.domain.member.entity.Member;

public class MemberFixture {

	public static Member createMemberEntity() {
		return Member.createMember(createSignUpRequest().build(), "encodedTestPassword");
	}

	public static SignUpRequestBuilder createSignUpRequest() {
		return new SignUpRequestBuilder();
	}

	public static UpdateRequestBuilder createUpdateMemberRequest() {
		return new UpdateRequestBuilder();
	}
}
