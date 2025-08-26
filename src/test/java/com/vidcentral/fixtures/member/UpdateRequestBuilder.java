package com.vidcentral.fixtures.member;

import static com.vidcentral.fixtures.FixtureConstant.*;

import com.vidcentral.api.dto.member.request.UpdateMemberRequest;

public class UpdateRequestBuilder {

	private String nickname = UPDATED_NICKNAME;
	private String introduce = UPDATED_INTRODUCE;

	public UpdateRequestBuilder nickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public UpdateRequestBuilder introduce(String introduce) {
		this.introduce = introduce;
		return this;
	}

	public UpdateMemberRequest build() {
		return new UpdateMemberRequest(nickname, introduce);
	}
}
