package com.vidcentral.api.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vidcentral.api.application.auth.AuthenticationService;
import com.vidcentral.api.dto.auth.request.LoginRequest;
import com.vidcentral.api.dto.auth.response.TokenResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "인증 APIs", description = "로그인 및 재발급 API")
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하여 액세스/리프레시 토큰을 발급합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 로그인 성공"),
		@ApiResponse(responseCode = "400", description = "️❌ 비밀번호 확인 불일치"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 회원"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<TokenResponse> loginMember(@Valid @RequestBody LoginRequest loginRequest) {
		return ResponseEntity.ok().body(authenticationService.loginMember(loginRequest));
	}

	@PostMapping("/reissue")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "토큰 재발급", description = "리프레시 토큰으로 새로운 액세스/리프레시 토큰을 발급합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 재발급 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 비밀번호 확인 불일치"),
		@ApiResponse(responseCode = "401", description = "🔒 만료되었거나 위조된 토큰"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 회원"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<TokenResponse> reissueToken(@RequestHeader("Authorization") String refreshToken) {
		return ResponseEntity.ok().body(authenticationService.reissueTokens(refreshToken));
	}
}
