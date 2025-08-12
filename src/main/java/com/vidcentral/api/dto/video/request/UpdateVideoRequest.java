package com.vidcentral.api.dto.video.request;

import java.util.List;

import com.vidcentral.api.domain.video.model.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "비디오 수정 요청 DTO")
public record UpdateVideoRequest(
	@Schema(description = "비디오 제목", example = "videoTitle")
	String title,

	@Schema(description = "비디오 설명", example = "videoDescription")
	String description,

	@Schema(description = "비디오 썸네일 경로", example = "/path/to/thumbnail.jpg")
	String thumbnailPath,

	@Schema(description = "비디오 카테고리", example = "IT_SOFTWARE")
	Category category,

	@Schema(description = "비디오 태그 목록", example = "Java, Spring, Framework")
	List<String> tags
) {
}
