package com.vidcentral.api.domain.member.entity;

import static com.vidcentral.global.common.util.GlobalConstant.*;
import static com.vidcentral.global.common.util.MediaConstant.*;
import static java.util.Objects.*;

import com.vidcentral.api.dto.member.request.SignUpRequest;
import com.vidcentral.global.common.entity.BaseTimeEntity;
import com.vidcentral.api.infrastructure.converter.AESGcmAttributeConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "MEMBERS")
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

	@Column(name = "resident_number", nullable = false)
	@Convert(converter = AESGcmAttributeConverter.class)
	private String residentNumber;

	@Column(name = "hash_resident_number", nullable = false, unique = true)
	private String hashResidentNumber;

	@Column(name = "introduce", length = 50)
	private String introduce;

	@Column(name = "profile_image_url", nullable = false)
	private String profileImageURL;

	private Member(String email, String password, String nickname, String residentNumber, String hashResidentNumber) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.residentNumber = residentNumber;
		this.hashResidentNumber = hashResidentNumber;
		this.introduce = INTRODUCE_ME;
		this.profileImageURL = IMAGE_DOMAIN + MEMBER_PROFILE_URL;
	}

	public static Member createMember(SignUpRequest signUpRequest, String encodedPassword, String hashResidentNumber) {
		return new Member(
			signUpRequest.email(),
			encodedPassword,
			signUpRequest.nickname(),
			signUpRequest.residentNumber(),
			hashResidentNumber
		);
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updateIntroduce(String introduce) {
		this.introduce = requireNonNullElse(introduce, this.introduce);
	}

	public void updateProfileImageURL(String newProfileImageURL) {
		this.profileImageURL = requireNonNullElse(newProfileImageURL, this.profileImageURL);
	}
}
