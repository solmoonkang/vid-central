package com.vidcentral.api.application.media.strategy;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.vidcentral.api.domain.media.entity.Attachment;
import com.vidcentral.api.domain.media.entity.Media;
import com.vidcentral.api.domain.media.model.MediaType;
import com.vidcentral.api.domain.media.repository.AttachmentRepository;
import com.vidcentral.api.infrastructure.s3.S3StorageService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VideoAttachment implements AttachmentStrategy {

	private final S3StorageService s3StorageService;
	private final AttachmentRepository attachmentRepository;

	@Override
	public MediaType supportType() {
		return MediaType.VIDEO;
	}

	@Override
	public void handle(Media media, List<MultipartFile> mediaFiles, MultipartFile thumbnailFile) {
		if (mediaFiles == null || mediaFiles.size() != 1) {
			throw new IllegalArgumentException("[❎ ERROR] 동영상 파일은 하나만 업로드해야 합니다.");
		}

		if (thumbnailFile == null) {
			throw new IllegalArgumentException("[❎ ERROR] 동영상 썸네일을 업로드해주세요.");
		}

		final String videoURL = s3StorageService.storeMediaFile(mediaFiles.get(0), media.getId().toString());
		final String thumbnailURL = s3StorageService.storeThumbnailFile(thumbnailFile, media.getId().toString());
		attachmentRepository.save(Attachment.createVideoAttachment(media, videoURL, thumbnailURL));
	}
}
