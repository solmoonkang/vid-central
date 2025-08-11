package com.vidcentral.api.application.video;

import org.springframework.stereotype.Service;

import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.application.video.upload.VideoUploadManager;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.domain.video.repository.VideoRepository;
import com.vidcentral.api.dto.video.request.UploadVideoRequest;
import com.vidcentral.global.auth.model.AuthMember;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoWriteService {

	private final MemberReadService memberReadService;
	private final VideoUploadManager videoUploadManager;
	private final VideoRepository videoRepository;

	public void uploadVideo(AuthMember authMember, UploadVideoRequest uploadVideoRequest) {
		final Member member = memberReadService.getMemberById(authMember.id());

		final String originalFileName = getOriginalFileName(uploadVideoRequest);
		final Video video = Video.createVideo(
			member, uploadVideoRequest.title(), uploadVideoRequest.description(), originalFileName,
			uploadVideoRequest.category(), uploadVideoRequest.tags());
		videoRepository.save(video);

		videoUploadManager.processVideoUpload(video.getId(), uploadVideoRequest);
	}

	private String getOriginalFileName(UploadVideoRequest uploadVideoRequest) {
		if (uploadVideoRequest.originalFileName() == null || uploadVideoRequest.originalFileName().trim().isEmpty())
			return uploadVideoRequest.videoFile().getOriginalFilename();

		return uploadVideoRequest.originalFileName();
	}
}
