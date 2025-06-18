package com.vidcentral.api.dto.subscription.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "구독 또는 구독취소 응답 DTO")
public record SubscriptionResponse(
	@Schema(description = "구독 또는 구독취소 상태", example = "true/false")
	boolean subscribed
) {
}
