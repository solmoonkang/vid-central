package com.vidcentral.api.application.video.mapper;

import java.util.List;

import com.vidcentral.api.dto.video.request.UploadVideoRequest;
import com.vidcentral.api.dto.video.response.VideoProcessingMessage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoMapper {

	public static VideoProcessingMessage toVideoProcessingMessage(Long videoId, String filePath,
		UploadVideoRequest uploadVideoRequest, List<String> resolutions) {

		return new VideoProcessingMessage(videoId, filePath, uploadVideoRequest.originalFileName(), resolutions);
	}
}
