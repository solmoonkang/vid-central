package com.vidcentral.api.dto.history.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "시청 기록 수정 요청 DTO")
public record UpdateHistoryRequest(
	@Schema(description = "마지막 시청 시간 (초 단위)", example = "300L")
	Long lastWatchedTime,

	@Schema(description = "시청 완료 여부", example = "false")
	Boolean isFinished
) {
}
