package com.vidcentral.api.application.video;

import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.dto.request.video.UploadVideoRequest;
import com.vidcentral.api.dto.response.video.VideoInfoResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoMapper {

	public static Video toVideo(Member member, UploadVideoRequest uploadVideoRequest, String videoURL) {
		return Video.builder()
			.member(member)
			.title(uploadVideoRequest.title())
			.description(uploadVideoRequest.description())
			.videoURL(videoURL)
			.videoTags(uploadVideoRequest.videoTags())
			.build();
	}

	public static VideoInfoResponse toVideoInfoResponse(Video video) {
		return VideoInfoResponse.builder()
			.nickname(video.getMember().getNickname())
			.title(video.getTitle())
			.description(video.getDescription())
			.videoURL(video.getVideoURL())
			.build();
	}
}
