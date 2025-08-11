package com.vidcentral.api.dto.pagination;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "페이지네이션 정보 응답 DTO")
public record PaginationInfo(
	@Schema(description = "현재 페이지 번호", example = "1")
	int pageNumber,

	@Schema(description = "다음 페이지 존재 여부", example = "true")
	boolean hasNext
) {
}
