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
public class ImageAttachment implements AttachmentStrategy {

	private final S3StorageService s3StorageService;
	private final AttachmentRepository attachmentRepository;

	@Override
	public MediaType supportType() {
		return MediaType.IMAGE;
	}

	@Override
	public void handle(Media media, List<MultipartFile> mediaFiles, MultipartFile unusedThumbnailFile) {
		if (mediaFiles == null || mediaFiles.isEmpty()) {
			throw new IllegalArgumentException("[❎ ERROR] 이미지 파일은 최소 한 개 이상 업로드해야 합니다.");
		}

		for (MultipartFile mediaFile : mediaFiles) {
			final String imageURL = s3StorageService.storeMediaFile(mediaFile, media.getId().toString());
			attachmentRepository.save(Attachment.createImageAttachment(media, imageURL));
		}
	}
}
