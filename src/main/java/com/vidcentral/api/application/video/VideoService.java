package com.vidcentral.api.application.video;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.video.validator.VideoValidator;
import com.vidcentral.api.dto.video.request.UploadVideoRequest;
import com.vidcentral.global.auth.model.AuthMember;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoService {

	private final VideoValidator videoValidator;
	private final VideoWriteService videoWriteService;

	@Transactional
	public void uploadVideo(AuthMember authMember, UploadVideoRequest uploadVideoRequest) {
		videoValidator.validateUploadVideoRequest(uploadVideoRequest);
		videoWriteService.uploadVideo(authMember, uploadVideoRequest);
	}
}
