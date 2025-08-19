package com.vidcentral.global.error.model;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

	// 400 Bad Request
	INVALID_REQUEST_FIELD(BAD_REQUEST, "400_001", "잘못된 요청입니다. 입력한 데이터가 유효하지 않습니다."),
	INVALID_PASSWORD(BAD_REQUEST, "400_002", "입력하신 비밀번호는 틀린 비밀번호입니다."),
	INVALID_TOKEN(BAD_REQUEST, "400_003", "유효하지 않은 토큰입니다."),
	INVALID_IMAGE_EXTENSION(BAD_REQUEST, "400_004", "유효하지 않은 이미지 확장자입니다."),
	INVALID_VIDEO(BAD_REQUEST, "400_005", "유효하지 않은 비디오 파일입니다."),
	INVALID_RESOLUTION(BAD_REQUEST, "400_006", "%s는 지원하지 않는 해상도입니다."),
	INVALID_CHUNK_NUMBER_RANGE(BAD_REQUEST, "400_007", "청크 번호는 전체 청크 수를 초과할 수 없습니다."),
	TOTAL_CHUNKS_MISMATCH(BAD_REQUEST, "400_008", "총 청크 수가 일치하지 않습니다."),
	DUPLICATE_CHUNK_UPLOAD(BAD_REQUEST, "400_009", "이미 업로드된 청크입니다."),
	PASSWORD_MISMATCH(BAD_REQUEST, "400_010", "비밀번호와 확인 비밀번호가 일치하지 않습니다."),
	INVALID_FILE(BAD_REQUEST, "400_011", "파일이 비어있습니다."),

	// 401 Unauthorized
	TOKEN_EXPIRED(UNAUTHORIZED, "401_001", "이미 만료된 토큰입니다."),
	UNAUTHORIZED_MEMBER(UNAUTHORIZED, "401_002", "인증되지 않은 사용자입니다."),

	// 403 Forbidden
	FORBIDDEN_VIDEO_MODIFY(FORBIDDEN, "403_001", "비디오 수정 접근 권한이 없습니다."),
	FORBIDDEN_COMMENT_MODIFY(FORBIDDEN, "403_002", "댓글 수정 접근 권한이 없습니다."),
	FORBIDDEN_COMMENT_DELETE(FORBIDDEN, "403_003", "댓글 삭제 접근 권한이 없습니다."),

	// 404 Not Found
	MEMBER_NOT_FOUND(NOT_FOUND, "404_001", "존재하지 않는 회원입니다."),
	VIDEO_NOT_FOUND(NOT_FOUND, "404_002", "존재하지 않는 비디오입니다."),
	COMMENT_NOT_FOUND(NOT_FOUND, "404_003", "존재하지 않는 댓글입니다."),
	VIEWING_HISTORY_NOT_FOUND(NOT_FOUND, "404_004", "존재하지 않는 시청 기록입니다."),
	TOKEN_NOT_FOUND(NOT_FOUND, "404_005", "존재하지 않는 토큰입니다."),

	// 409 Conflict
	EMAIL_DUPLICATED(CONFLICT, "409_001", "이미 사용중인 이메일입니다."),
	NICKNAME_DUPLICATED(CONFLICT, "409_002", "이미 사용중인 닉네임입니다."),

	// 500 Internal Server Error
	MESSAGE_SEND_FAILED(INTERNAL_SERVER_ERROR, "500_001", "메시지 큐 전송에 실패했습니다."),
	FILE_SAVE_FAILED(INTERNAL_SERVER_ERROR, "500_002", "파일 저장에 실패했습니다."),
	FILE_MERGE_FAILED(INTERNAL_SERVER_ERROR, "500_003", "파일 병합에 실패했습니다."),
	FILE_DELETE_FAILED(INTERNAL_SERVER_ERROR, "500_004", "파일 삭제에 실패했습니다."),
	VIDEO_PROCESSING_FAILED(INTERNAL_SERVER_ERROR, "500_005", "FFmpeg 명령어 실행에 실패했습니다."),
	THUMBNAIL_CREATION_FAILED(INTERNAL_SERVER_ERROR, "500_006", "썸네일 생성에 실패했습니다."),
	DIRECTORY_CREATION_FAILED(INTERNAL_SERVER_ERROR, "500_007", "%s 경로의 디렉토리 생성에 실패했습니다."),
	DIRECTORY_DELETE_FAILED(INTERNAL_SERVER_ERROR, "500_008", "%s 경로의 디렉토리 삭제에 실패했습니다."),
	UNKNOWN_SERVER_ERROR(INTERNAL_SERVER_ERROR, "500_999", "서버에서 알 수 없는 에러가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	public String getFormattedMessage(Object... args) {
		return String.format(this.message, args);
	}
}
