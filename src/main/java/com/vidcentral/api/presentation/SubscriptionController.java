package com.vidcentral.api.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vidcentral.api.application.subscription.SubscriptionService;
import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.dto.subscription.response.SubscriptionResponse;
import com.vidcentral.global.auth.annotation.Auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class SubscriptionController {

	private final SubscriptionService subscriptionService;

	@PostMapping("/{channelOwnerId}")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "구독/구독취소",
		description = "로그인된 사용자는 채널 주인을 구독 또는 구독취소 할 수 있습니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "✅ 구독 또는 구독취소 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 요청 형식 오류"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자 요청"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자 또는 미디어 오류"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류"),
	})
	public ResponseEntity<SubscriptionResponse> toggleReaction(
		@Auth AuthMember authMember,
		@PathVariable Long channelOwnerId) {

		return ResponseEntity.ok().body(subscriptionService.toggleSubscription(authMember, channelOwnerId));
	}
}
