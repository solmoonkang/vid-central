package com.vidcentral.api.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vidcentral.api.application.auth.AuthenticationService;
import com.vidcentral.api.dto.auth.request.LoginRequest;
import com.vidcentral.api.dto.auth.response.LoginResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 APIs", description = "로그인, 토급 재발급 기능을 제공합니다.")
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "로그인",
		description = "사용자가 이메일, 비밀번호를 검증한 뒤, AccessToken과 RefreshToken을 발급합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "✅ 로그인 성공 및 토큰 발급"),
		@ApiResponse(responseCode = "400", description = "❌ 형식 오류 또는 비밀번호 불일치"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<LoginResponse> loginMember(
		HttpServletResponse httpServletResponse,
		@RequestBody @Valid LoginRequest loginRequest) {

		return ResponseEntity.ok().body(authenticationService.loginMember(httpServletResponse, loginRequest));
	}
}
