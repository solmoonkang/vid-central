package com.vidcentral.api.dto.video.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "메시지 큐에 보낼 메시지 응답 DTO")
public record VideoProcessingMessage(
	@Schema(description = "비디오 ID", example = "1L")
	Long videoId,

	@Schema(description = "비디오 파일 경로")
	String filePath
) {
}
