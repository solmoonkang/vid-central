package com.vidcentral.api.domain.video.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vidcentral.api.dto.video.response.VideoDetailResponse;
import com.vidcentral.api.dto.video.response.VideoResponse;

public interface VideoRepositoryCustom {

	Page<VideoResponse> findVideoResponseByKeyword(String keyword, Pageable pageable);

	Optional<VideoDetailResponse> findVideoDetailWithUploader(Long videoId);
}
