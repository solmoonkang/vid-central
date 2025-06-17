package com.vidcentral.api.dto.media.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.vidcentral.api.domain.media.model.MediaType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "업로드 요청 DTO")
public record UploadMediaRequest(
	@Schema(description = "업로드 제목", example = "videoTitle")
	@NotBlank(message = "비디오 제목을 입력해주세요.")
	String title,

	@Schema(description = "업로드 내용", example = "videoContent")
	@NotBlank(message = "비디오 내용을 입력해주세요.")
	String content,

	@Schema(description = "업로드 타입 (TEXT, IMAGE, VIDEO)", example = "VIDEO")
	@NotNull(message = "업로드 타입을 입력해주세요.")
	MediaType mediaType,

	@Schema(
		description = "미디어 파일 목록\n" +
			"- TEXT: 비어 있어도 무방\n" +
			"- IMAGE: 하나 이상의 이미지 파일\n" +
			"- VIDEO: 동영상 파일 1개"
	)
	List<MultipartFile> mediaFiles,

	@Schema(
		description = "썸네일 파일\n" +
			"- VIDEO 타입에서만 사용\n" +
			"- IMAGE, TEXT 타입일 경우 null"
	)
	MultipartFile thumbnailFile
) {
}
