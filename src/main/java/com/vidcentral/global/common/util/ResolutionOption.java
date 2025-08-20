package com.vidcentral.global.common.util;

import java.util.Arrays;

import com.vidcentral.global.error.exception.BadRequestException;
import com.vidcentral.global.error.model.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResolutionOption {

	P480("480p", 480),
	P720("720p", 720),
	P1080("1080p", 1080);

	private final String value;
	private final int height;

	public static ResolutionOption fromValue(String value) {
		return Arrays.stream(values())
			.filter(resolution -> resolution.value.equals(value))
			.findFirst()
			.orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_RESOLUTION, value));
	}
}
