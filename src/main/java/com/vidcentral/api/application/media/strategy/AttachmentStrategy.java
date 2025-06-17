package com.vidcentral.api.application.media.strategy;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.vidcentral.api.domain.media.entity.Media;
import com.vidcentral.api.domain.media.model.MediaType;

public interface AttachmentStrategy {

	MediaType supportType();

	void handle(Media media, List<MultipartFile> mediaFiles, MultipartFile thumbnailFile);
}
