package com.vidcentral.api.application.video.mapper;

import com.vidcentral.api.dto.video.response.VideoProcessingMessage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoMapper {

	public static VideoProcessingMessage toVideoProcessingMessage(Long videoId, String filePath) {
		return new VideoProcessingMessage(videoId, filePath);
	}
}
