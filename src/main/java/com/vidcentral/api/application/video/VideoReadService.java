package com.vidcentral.api.application.video;

import org.springframework.stereotype.Service;

import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.domain.video.repository.VideoRepository;
import com.vidcentral.global.error.exception.NotFoundException;
import com.vidcentral.global.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoReadService {

	private final VideoRepository videoRepository;

	public Video getVideoById(Long videoId) {
		return videoRepository.findById(videoId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.VIDEO_NOT_FOUND));
	}
}
