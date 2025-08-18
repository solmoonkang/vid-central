package com.vidcentral.api.dto.video.request;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "비디오 청크 업로드 요청 DTO")
public record UploadChunkRequest(
	@NotNull(message = "비디오 파일을 등록해주세요.")
	@Schema(description = "비디오 파일")
	MultipartFile videoFile,

	@NotBlank(message = "사용자 지정 파일 이름을 입력해주세요.")
	@Schema(description = "사용자 지정 파일 이름", example = "videoFileName")
	String originalFileName,

	@Min(value = 1, message = "청크 번호는 1 이상이어야 합니다.")
	@Schema(description = "현재 청크 번호", example = "1")
	int chunkNumber,

	@Min(value = 1, message = "전체 청크 수는 1 이상이어야 합니다.")
	@Schema(description = "전체 청크 수", example = "10")
	int totalChunks
) {
}
