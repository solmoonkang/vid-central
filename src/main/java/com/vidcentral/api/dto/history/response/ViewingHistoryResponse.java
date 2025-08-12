package com.vidcentral.api.dto.history.response;

import java.time.LocalDateTime;

import com.vidcentral.api.dto.video.response.VideoResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "시청 기록 조회 응답 DTO")
public record ViewingHistoryResponse(
	@Schema(description = "시청 기록 ID", example = "1L")
	Long historyId,

	@Schema(description = "마지막 시청 시간 (초 단위)", example = "1200L")
	Long lastWatchedTime,

	@Schema(description = "시청 완료 여부", example = "false")
	Boolean isFinished,

	@Schema(description = "마지막 시청 기록 시간", example = "2023-01-01T10:00:00")
	LocalDateTime watchedAt,

	@Schema(description = "비디오 조회 응답 DTO")
	VideoResponse videoResponse
) {
}
