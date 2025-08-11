package com.vidcentral.api.presentation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vidcentral.api.application.video.VideoService;
import com.vidcentral.api.dto.pagination.PagedResponse;
import com.vidcentral.api.dto.video.request.UploadVideoRequest;
import com.vidcentral.api.dto.video.response.VideoListResponse;
import com.vidcentral.global.auth.annotation.Auth;
import com.vidcentral.global.auth.model.AuthMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
@Tag(name = "비디오 APIs", description = "비디오 업로드, 목록 조회 등 비디오 관련 API")
public class VideoController {

	private final VideoService videoService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "비디오 업로드", description = "사용자 인증 후 비디오를 업로드합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "🎉 비디오 업로드 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 유효성 검사 실패"),
		@ApiResponse(responseCode = "401", description = "🔒 만료되었거나 위조된 토큰"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<String> uploadVideo(
		@Auth AuthMember authMember,
		@Valid @ModelAttribute UploadVideoRequest uploadVideoRequest) {

		videoService.uploadVideo(authMember, uploadVideoRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 성공적으로 비디오를 업로드했습니다.");
	}

	@GetMapping("/recommend")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "추천 비디오 목록 조회", description = "로그인한 사용자의 시청 기록을 기반으로 비디오를 추천합니다. 시청 기록이 없거나 비로그인 사용자는 빈 목록을 반환합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "🎉 추천 비디오 목록 조회 성공"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<PagedResponse<VideoListResponse>> getRecommendedVideosForUser(@Auth AuthMember authMember) {
		return ResponseEntity.ok().body(videoService.getRecommendedVideosForUser(authMember));
	}

	@GetMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "검색한 비디오 목록 조회", description = "비디오 제목 또는 업로더 닉네임을 기준으로 비디오 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "🎉 검색한 비디오 목록 조회 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 검색어 누락"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<PagedResponse<VideoListResponse>> getSearchVideos(
		@RequestParam @NotBlank(message = "검색어를 입력해주세요.") String keyword,
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

		return ResponseEntity.ok().body(videoService.getSearchVideos(keyword, pageable));
	}
}
