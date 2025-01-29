package com.vidcentral.api.dto.request.video;

import java.util.List;

import com.vidcentral.api.domain.video.entity.VideoTag;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateVideoRequest(
	@NotBlank(message = "비디오 제목은 필수 입력 항목입니다.")
	String title,

	@NotBlank(message = "비디오 설명은 필수 입력 항목입니다.")
	String description,

	List<VideoTag> videoTags
) {
}
