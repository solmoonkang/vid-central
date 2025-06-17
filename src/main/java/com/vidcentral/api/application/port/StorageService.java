package com.vidcentral.api.application.port;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	String storeMediaFile(MultipartFile multipartFile, String mediaId);

	String storeThumbnailFile(MultipartFile multipartFile, String mediaId);
}
