package com.vidcentral.api.application.video.validator;

import org.springframework.stereotype.Component;

import com.vidcentral.global.error.exception.BadRequestException;
import com.vidcentral.global.error.exception.UnauthorizedException;
import com.vidcentral.global.error.model.ErrorMessage;

@Component
public class VideoValidator {

	public void validateChunkIntegrity(int chunkNumber, int totalChunks) {
		if (chunkNumber > totalChunks) {
			throw new BadRequestException(ErrorMessage.INVALID_CHUNK_NUMBER_RANGE);
		}
	}

	public void validateVideoOwner(Long videoOwnerId, Long authMemberId) {
		if (!videoOwnerId.equals(authMemberId)) {
			throw new UnauthorizedException(ErrorMessage.FORBIDDEN_VIDEO_MODIFY);
		}
	}
}
