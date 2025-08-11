package com.vidcentral.api.dto.pagination;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "페이징된 목록 데이터 응답 DTO")
public record PagedResponse<T>(
	@Schema(description = "현재 페이지의 데이터 목록")
	List<T> content,

	@Schema(description = "페이지네이션 정보")
	PaginationInfo paginationInfo
) {
}
