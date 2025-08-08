package com.vidcentral.api.application.video.validator;

import org.springframework.stereotype.Component;

import com.vidcentral.api.dto.video.request.UploadVideoRequest;
import com.vidcentral.global.error.exception.BadRequestException;
import com.vidcentral.global.error.model.ErrorMessage;

@Component
public class VideoValidator {

	public void validateUploadVideoRequest(UploadVideoRequest uploadVideoRequest) {
		if (uploadVideoRequest.videoFile().isEmpty()) {
			throw new BadRequestException(ErrorMessage.INVALID_FILE);
		}
	}
}
