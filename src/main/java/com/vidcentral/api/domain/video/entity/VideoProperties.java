package com.vidcentral.api.domain.video.entity;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum VideoProperties {

	DEFAULT_VIDEO(100 * 1024 * 1024, "video/mp4"),
	AVI_VIDEO(100 * 1024 * 1024, "video/x-msvideo"),
	MKV_VIDEO(100 * 1024 * 1024, "video/x-matroska");

	private final long maxSize;
	private final String contentType;

	VideoProperties(long maxSize, String contentType) {
		this.maxSize = maxSize;
		this.contentType = contentType;
	}

	public static boolean isSupportedContentType(String contentType) {
		return Arrays.stream(values())
			.anyMatch(videoProperties -> videoProperties.contentType.equals(contentType));
	}
}
