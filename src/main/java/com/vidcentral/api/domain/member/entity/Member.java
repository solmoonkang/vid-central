package com.vidcentral.api.domain.member.entity;

import static com.vidcentral.global.common.util.GlobalConstant.*;
import static java.util.Objects.*;

import com.vidcentral.api.dto.member.request.SignUpRequest;
import com.vidcentral.global.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "MEMBERS", indexes = {
	@Index(name = "idx_member_email", columnList = "email", unique = true),
	@Index(name = "idx_member_nickname", columnList = "nickname", unique = true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "nickname", nullable = false, unique = true)
	private String nickname;

	@Column(name = "introduce", length = 50)
	private String introduce;

	@Column(name = "profile_image_url", nullable = false)
	private String profileImageURL;

	@Column(name = "refresh_token")
	private String refreshToken;

	private Member(String email, String password, String nickname, String introduce, String profileImageURL) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.introduce = introduce;
		this.profileImageURL = profileImageURL;
	}

	public static Member createMember(SignUpRequest signUpRequest, String encodedPassword) {
		return new Member(
			signUpRequest.email(),
			encodedPassword,
			signUpRequest.nickname(),
			INTRODUCE_ME,
			IMAGE_DOMAIN + MEMBER_PROFILE_URL);
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updateIntroduce(String introduce) {
		this.introduce = requireNonNullElse(introduce, this.introduce);
	}

	public void updateMemberRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
