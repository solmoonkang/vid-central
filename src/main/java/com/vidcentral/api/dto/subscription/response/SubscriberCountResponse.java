package com.vidcentral.api.dto.subscription.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "채널 구독자 수 응답 DTO")
public record SubscriberCountResponse(
	@Schema(description = "채널 구독자 수", example = "999")
	long subscriberCount
) {
}
