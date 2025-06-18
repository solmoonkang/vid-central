package com.vidcentral.api.application.media;

import static com.vidcentral.global.error.model.ErrorMessage.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.domain.media.entity.Media;
import com.vidcentral.api.domain.media.repository.MediaRepository;
import com.vidcentral.global.error.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaReadService {

	private final MediaRepository mediaRepository;

	public Media readMedia(Long mediaId) {
		return mediaRepository.findById(mediaId)
			.orElseThrow(() -> new NotFoundException(FAILED_VIDEO_NOT_FOUND_ERROR));
	}
}
