package com.vidcentral.api.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vidcentral.api.application.media.MediaService;
import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.dto.media.request.UploadMediaRequest;
import com.vidcentral.global.auth.annotation.Auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@Tag(name = "미디어 APIs", description = "영상 또는 이미지 업로드 및 조회 기능을 제공합니다.")
public class MediaController {

	private final MediaService mediaService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "영상 및 이미지 업로드",
		description = "로그인된 사용자는 비디오 영상 또는 이미지를 업로드할 수 있습니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "✅ 업로드 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 요청 형식 오류"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자 오류"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류"),
	})
	public ResponseEntity<String> uploadMedia(
		@Auth AuthMember authMember,
		@Valid @ModelAttribute UploadMediaRequest uploadMediaRequest) {

		mediaService.uploadMedia(authMember, uploadMediaRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 영상 및 이미지를 성공적으로 업로드했습니다.");
	}
}
