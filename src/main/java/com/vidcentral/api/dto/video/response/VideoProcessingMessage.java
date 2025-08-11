package com.vidcentral.api.dto.video.response;

import java.util.List;

import com.vidcentral.api.domain.video.model.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "메시지 큐에 보낼 메시지 DTO")
public record VideoProcessingMessage(
	@Schema(description = "비디오 ID", example = "1L")
	Long videoId,

	@Schema(description = "비디오 파일 경로")
	String filePath,

	@Schema(description = "사용자 지정 파일 이름", example = "videoFileName")
	String originalFileName,

	@Schema(description = "비디오 해상도 목록", example = "480p, 720p, 1080p")
	List<String> resolutions,

	@Schema(description = "비디오 카테고리", example = "IT_SOFTWARE")
	Category category,

	@Schema(description = "비디오 태그 목록", example = "Java, Spring, Framework")
	List<String> tags
) {
}
