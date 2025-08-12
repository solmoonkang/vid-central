package com.vidcentral.global.error.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorMessage {

	INVALID_REQUEST_FIELD("[❎ ERROR] 잘못된 요청입니다. 입력한 데이터가 유효하지 않습니다."),
	INVALID_PASSWORD("[❎ ERROR] 입력하신 비밀번호는 틀린 비밀번호입니다."),
	INVALID_TOKEN("[❎ ERROR] 유효하지 않은 토큰입니다."),
	INVALID_IMAGE_EXTENSION("[❎ ERROR] 유효하지 않은 이미지 확장자입니다."),
	INVALID_VIDEO("[❎ ERROR] 유효하지 않은 비디오 파일입니다."),
	INVALID_VIDEO_ACCESS("[❎ ERROR] 유효하지 않은 비디오 접근입니다."),
	INVALID_COMMENT_ACCESS("[❎ ERROR] 유효하지 않은 댓글 접근입니다."),
	PASSWORD_MISMATCH("[❎ ERROR] 비밀번호와 확인 비밀번호가 일치하지 않습니다."),
	INVALID_FILE("[❎ ERROR] 파일이 비어있습니다."),
	INVALID_RESOLUTION("[❎ ERROR] 지원하지 않는 해상도입니다."),

	TOKEN_EXPIRED("[❎ ERROR] 이미 만료된 토큰입니다."),
	UNAUTHORIZED_MEMBER("[❎ ERROR] 인증되지 않은 사용자입니다."),

	FORBIDDEN_VIDEO_MODIFY("[❎ ERROR] 비디오 수정 접근 권한이 없습니다."),
	FORBIDDEN_COMMENT_MODIFY("[❎ ERROR] 댓글 수정 접근 권한이 없습니다."),
	FORBIDDEN_COMMENT_DELETE("[❎ ERROR] 댓글 삭제 접근 권한이 없습니다."),

	MEMBER_NOT_FOUND("[❎ ERROR] 존재하지 않는 회원입니다."),
	VIDEO_NOT_FOUND("[❎ ERROR] 존재하지 않는 비디오입니다."),
	COMMENT_NOT_FOUND("[❎ ERROR] 존재하지 않는 댓글입니다."),
	VIEWING_HISTORY_NOT_FOUND("[❎ ERROR] 존재하지 않는 시청 기록입니다."),
	TOKEN_NOT_FOUND("[❎ ERROR] 존재하지 않는 토큰입니다."),

	EMAIL_DUPLICATED("[❎ ERROR] 이미 사용중인 이메일입니다."),
	NICKNAME_DUPLICATED("[❎ ERROR] 이미 사용중인 닉네임입니다."),

	MESSAGE_SEND_ERROR("[❎ ERROR] 메시지 큐 전송에 실패했습니다."),
	FILE_SAVE_ERROR("[❎ ERROR] 임시 파일 저장에 실패했습니다."),
	FILE_DELETE_ERROR("[❎ ERROR] 파일 삭제에 실패했습니다."),
	VIDEO_PROCESSING_ERROR("[❎ ERROR] FFmpeg 명령어 실행에 실패했습니다."),
	UNKNOWN_SERVER_ERROR("[❎ ERROR] 서버에서 알 수 없는 에러가 발생했습니다.");

	private final String message;
}
