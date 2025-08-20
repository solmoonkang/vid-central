package com.vidcentral.global.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ThumbnailOption {

	SEEK_POSITION("-ss"),
	FRAMES_TO_EXTRACT("-vframes");

	private final String command;
}
