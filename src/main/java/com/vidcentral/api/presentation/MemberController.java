package com.vidcentral.api.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vidcentral.api.application.member.MemberService;
import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.dto.member.request.SignUpRequest;
import com.vidcentral.api.dto.member.request.UpdateMemberRequest;
import com.vidcentral.api.dto.member.response.MemberInfoResponse;
import com.vidcentral.global.auth.annotation.Auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Tag(name = "회원 APIs", description = "회원가입 및 회원 정보 관리 API")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "회원가입",
		description = "요청된 사용자 정보를 바탕으로 신규 회원 계정을 생성합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "🎉 회원가입 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 형식 오류 또는 비밀번호 불일치"),
		@ApiResponse(responseCode = "409", description = "⚠️ 중복된 사용자 정보"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<String> signUpMember(@RequestBody @Valid SignUpRequest signUpRequest) {
		memberService.signUpMember(signUpRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 성공적으로 회원가입이 완료되었습니다.");
	}

	@GetMapping("/{memberId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "내 정보 조회",
		description = "인증된 사용자 본인의 닉네임, 주민등록번호, 가입일을 반환합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "✅ 내 정보 조회 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 형식 오류"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<MemberInfoResponse> searchMemberInfo(@PathVariable(required = false) Long memberId) {
		return ResponseEntity.ok(memberService.searchMemberInfo(memberId));
	}

	@PutMapping("/update")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "내 정보 수정",
		description = "사용자가 닉네임, 소개글, 프로필 이미지 정보를 수정합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = ""),
		@ApiResponse(responseCode = "400", description = ""),
		@ApiResponse(responseCode = "404", description = ""),
		@ApiResponse(responseCode = "409", description = ""),
		@ApiResponse(responseCode = "500", description = "")
	})
	public ResponseEntity<String> updateMemberInfo(
		@Auth AuthMember authMember,
		@Valid @RequestPart(required = false) UpdateMemberRequest updateMemberRequest,
		@RequestPart(name = "profileImageURL") MultipartFile newProfileImageURL) {

		memberService.updateMemberInfo(authMember, updateMemberRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 성공적으로 회원 정보가 업데이트되었습니다.");
	}
}
