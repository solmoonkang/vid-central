package com.vidcentral.api.application.video.mapper;

import java.util.List;

import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.dto.comment.response.CommentResponse;
import com.vidcentral.api.dto.video.request.UploadVideoRequest;
import com.vidcentral.api.dto.video.response.VideoDetailResponse;
import com.vidcentral.api.dto.video.response.VideoListResponse;
import com.vidcentral.api.dto.video.response.VideoProcessingMessage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoMapper {

	public static VideoProcessingMessage toVideoProcessingMessage(Long videoId, String filePath,
		UploadVideoRequest uploadVideoRequest, List<String> resolutions) {

		return new VideoProcessingMessage(videoId, filePath, uploadVideoRequest.originalFileName(), resolutions,
			uploadVideoRequest.category(), uploadVideoRequest.tags());
	}

	public static VideoListResponse toVideoResponse(Video video) {
		return new VideoListResponse(video.getId(), video.getThumbnailPath(), video.getTitle(),
			video.getMember().getNickname(), video.getViewCount(), video.getCreatedAt());
	}

	public static List<VideoListResponse> toVideoListResponse(List<Video> videos) {
		return videos.stream()
			.map(VideoMapper::toVideoResponse)
			.toList();
	}

	public static VideoDetailResponse toVideoDetailResponse(Video video, List<CommentResponse> commentResponseList) {
		return new VideoDetailResponse(video.getId(), video.getTitle(), video.getDescription(), video.getCreatedAt(),
			video.getFilePath(), video.getThumbnailPath(), video.getViewCount(), video.getMember().getNickname(),
			video.getLikeCount(), video.getDislikeCount(), commentResponseList);
	}
}
