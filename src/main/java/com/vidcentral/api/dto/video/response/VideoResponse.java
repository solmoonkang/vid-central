package com.vidcentral.api.dto.video.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "비디오 조회 응답 DTO")
public record VideoResponse(
	@Schema(description = "비디오 ID", example = "1L")
	Long videoId,

	@Schema(description = "비디오 썸네일 경로", example = "/path/to/thumbnail.jpg")
	String thumbnailPath,

	@Schema(description = "비디오 제목", example = "videoTitle")
	String title,

	@Schema(description = "비디오 업로더 닉네임", example = "memberNickname")
	String uploaderNickname,

	@Schema(description = "비디오 조회수", example = "100L")
	Long viewCount,

	@Schema(description = "비디오 업로드 날짜", example = "2023-01-01T10:00:00")
	LocalDateTime uploadedAt
) {
}
