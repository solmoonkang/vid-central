package com.vidcentral.api.dto.video.request;

import java.util.List;

import com.vidcentral.api.domain.video.model.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "비디오 메타데이터 업로드 요청 DTO")
public record CreateVideoRequest(
	@NotBlank(message = "비디오 제목을 입력해주세요.")
	@Schema(description = "비디오 제목", example = "videoTitle")
	String title,

	@NotBlank(message = "비디오 설명을 입력해주세요.")
	@Schema(description = "비디오 설명", example = "videoDescription")
	String description,

	@Schema(description = "비디오 해상도 목록", example = "480p, 720p, 1080p")
	List<String> resolutions,

	@NotNull(message = "비디오 카테고리를 선택해주세요.")
	@Schema(description = "비디오 카테고리", example = "IT_SOFTWARE")
	Category category,

	@Schema(description = "비디오 태그 목록", example = "Java, Spring, Framework")
	List<String> tags
) {
}

