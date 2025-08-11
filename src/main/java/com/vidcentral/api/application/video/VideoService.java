package com.vidcentral.api.application.video;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.recommend.RecommendationService;
import com.vidcentral.api.application.video.mapper.VideoMapper;
import com.vidcentral.api.application.video.validator.VideoValidator;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.dto.pagination.PagedResponse;
import com.vidcentral.api.dto.pagination.PaginationInfo;
import com.vidcentral.api.dto.video.request.UploadVideoRequest;
import com.vidcentral.api.dto.video.response.VideoListResponse;
import com.vidcentral.global.auth.model.AuthMember;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoService {

	private final VideoValidator videoValidator;
	private final VideoWriteService videoWriteService;
	private final RecommendationService recommendationService;

	@Transactional
	public void uploadVideo(AuthMember authMember, UploadVideoRequest uploadVideoRequest) {
		videoValidator.validateUploadVideoRequest(uploadVideoRequest);
		videoWriteService.uploadVideo(authMember, uploadVideoRequest);
	}

	public PagedResponse<VideoListResponse> getRecommendedVideosForUser(AuthMember authMember) {
		final List<Video> videos = recommendationService.getRecommendedVideos(authMember.id());
		final List<VideoListResponse> videoListResponses = VideoMapper.toVideoListResponse(videos);
		final PaginationInfo paginationInfo = new PaginationInfo(1, false);
		return new PagedResponse<>(videoListResponses, paginationInfo);
	}
}
