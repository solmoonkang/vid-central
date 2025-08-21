package com.vidcentral.api.domain.video.repository;

import java.util.Optional;

import com.vidcentral.api.dto.video.response.VideoDetailResponse;

public interface VideoRepositoryCustom {

	Optional<VideoDetailResponse> findVideoDetailWithUploader(Long videoId);
}
