package com.vidcentral.api.dto.video.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "비디오 업로드 요청 DTO")
public record UploadVideoRequest(
	@NotBlank(message = "비디오 제목을 입력해주세요.")
	@Schema(description = "비디오 제목", example = "videoTitle")
	String title,

	@NotBlank(message = "비디오 설명을 입력해주세요.")
	@Schema(description = "비디오 설명", example = "videoDescription")
	String description,

	@Schema(description = "사용자 지정 파일 이름", example = "videoFileName")
	String originalFileName,

	@Schema(description = "비디오 해상도 목록", example = "480p, 720p, 1080p")
	List<String> resolutions,

	@Schema(description = "비디오 파일")
	MultipartFile videoFile
) {
}
