package com.vidcentral.fixtures.member;

import static com.vidcentral.fixtures.FixtureConstant.*;

import com.vidcentral.api.dto.member.request.UpdateMemberRequest;

public class UpdateMemberRequestBuilder {

	private String nickname = UPDATED_NICKNAME;
	private String introduce = UPDATED_INTRODUCE;

	public UpdateMemberRequestBuilder nickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public UpdateMemberRequestBuilder introduce(String introduce) {
		this.introduce = introduce;
		return this;
	}

	public UpdateMemberRequest build() {
		return new UpdateMemberRequest(nickname, introduce);
	}
}
