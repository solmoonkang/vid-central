package com.vidcentral.api.application.media.strategy;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.vidcentral.api.domain.media.entity.Media;
import com.vidcentral.api.domain.media.model.MediaType;

@Component
public class TextAttachment implements AttachmentStrategy {

	@Override
	public MediaType supportType() {
		return MediaType.TEXT;
	}

	@Override
	public void handle(Media media, List<MultipartFile> unusedMediaFiles, MultipartFile unusedThumbnailFile) {
	}
}
