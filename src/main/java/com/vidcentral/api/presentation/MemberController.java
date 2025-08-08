package com.vidcentral.api.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vidcentral.api.application.member.MemberService;
import com.vidcentral.api.dto.member.request.SignUpRequest;
import com.vidcentral.api.dto.member.request.UpdateMemberRequest;
import com.vidcentral.global.auth.annotation.Auth;
import com.vidcentral.global.auth.model.AuthMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "회원 APIs", description = "회원 정보 저장 및 조회 관련 API")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "회원가입", description = "이메일, 비밀번호, 닉네임으로 회원가입을 수행합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "🎉 회원가입 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 비밀번호 확인 불일치"),
		@ApiResponse(responseCode = "409", description = "⚠️ 이미 존재하는 이메일 또는 닉네임"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<String> signUpMember(@Valid @RequestBody SignUpRequest signUpRequest) {
		memberService.signUpMember(signUpRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 성공적으로 회원 정보를 저장했습니다.");
	}

	@PutMapping("/update")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "회원 정보 수정", description = "닉네임, 소개글, 프로필 이미지 정보를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "🎉 회원 정보 수정 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 비밀번호 확인 불일치"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 회원"),
		@ApiResponse(responseCode = "409", description = "⚠️ 이미 존재하는 이메일 또는 닉네임"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<String> updateMember(
		@Auth AuthMember authMember,
		@Valid @RequestPart(required = false) UpdateMemberRequest updateMemberRequest) {

		memberService.updateMember(authMember, updateMemberRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 성공적으로 회원 정보를 수정했습니다.");
	}
}
