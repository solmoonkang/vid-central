package com.vidcentral.global.error.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorMessage {

	FAILED_INVALID_REQUEST_ERROR("[❎ ERROR] 잘못된 요청입니다. 입력한 데이터가 유효하지 않습니다."),
	FAILED_INVALID_PASSWORD_ERROR("[❎ ERROR] 입력하신 비밀번호는 틀린 비밀번호입니다."),
	FAILED_INVALID_TOKEN_ERROR("[❎ ERROR] 유효하지 않은 토큰입니다."),
	FAILED_INVALID_IMAGE_EXTENSION_ERROR("[❎ ERROR] 유효하지 않은 이미지 확장자입니다."),
	FAILED_INVALID_VIDEO_ERROR("[❎ ERROR] 유효하지 않은 비디오 파일입니다."),
	FAILED_INVALID_VIDEO_ACCESS_ERROR("[❎ ERROR] 유효하지 않은 비디오 접근입니다."),
	FAILED_INVALID_COMMENT_ACCESS_ERROR("[❎ ERROR] 유효하지 않은 댓글 접근입니다."),
	FAILED_MAX_TAG_COUNT_ERROR("[❎ ERROR] 태그는 최대 3개까지 설정할 수 있습니다."),
	FAILED_S3_RESIZE_ERROR("[❎ ERROR] S3 이미지 리사이즈에 실패했습니다."),
	FAILED_UNAUTHORIZED_MEMBER_ERROR("[❎ ERROR] 해당 사용자는 인증되지 않은 사용자입니다."),
	FAILED_EMAIL_DUPLICATION_ERROR("[❎ ERROR] 입력하신 이메일은 이미 존재하는 이메일입니다."),
	FAILED_NICKNAME_DUPLICATION_ERROR("[❎ ERROR] 입력하신 닉네임은 이미 존재하는 닉네임입니다."),
	FAILED_RESIDENT_NUMBER_DUPLICATION_ERROR("[❎ ERROR] 입력하신 주민번호는 이미 존재하는 주민번호입니다."),
	FAILED_PASSWORD_MISMATCH_ERROR("[❎ ERROR] 입력하신 비밀번호와 일치하지 않습니다."),
	FAILED_MEMBER_NOT_FOUND_ERROR("[❎ ERROR] 요청하신 사용자는 존재하지 않는 사용자입니다."),
	FAILED_MEDIA_NOT_FOUND_ERROR("[❎ ERROR] 요청하신 미디어는 존재하지 않는 미디어입니다."),
	FAILED_TOKEN_NOT_FOUND_ERROR("[❎ ERROR] JWT 토큰이 존재하지 않습니다."),
	FAILED_VIDEO_NOT_FOUND_ERROR("[❎ ERROR] 비디오 파일이 존재하지 않습니다."),

	FAILED_UNKNOWN_ERROR("[❎ ERROR] 서버에서 알 수 없는 에러가 발생했습니다.");

	private final String message;
}
