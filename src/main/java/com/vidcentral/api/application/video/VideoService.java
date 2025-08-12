package com.vidcentral.api.application.video;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.comment.CommentReadService;
import com.vidcentral.api.application.comment.mapper.CommentMapper;
import com.vidcentral.api.application.history.ViewingHistoryWriteService;
import com.vidcentral.api.application.recommend.RecommendationService;
import com.vidcentral.api.application.video.mapper.VideoMapper;
import com.vidcentral.api.application.video.validator.VideoValidator;
import com.vidcentral.api.domain.comment.entity.Comment;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.dto.comment.response.CommentResponse;
import com.vidcentral.api.dto.pagination.PagedResponse;
import com.vidcentral.api.dto.pagination.PaginationInfo;
import com.vidcentral.api.dto.video.request.UpdateVideoRequest;
import com.vidcentral.api.dto.video.request.UploadVideoRequest;
import com.vidcentral.api.dto.video.response.VideoDetailResponse;
import com.vidcentral.api.dto.video.response.VideoResponse;
import com.vidcentral.global.auth.model.AuthMember;
import com.vidcentral.global.error.exception.UnauthorizedException;
import com.vidcentral.global.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoService {

	private final VideoValidator videoValidator;
	private final VideoWriteService videoWriteService;
	private final VideoReadService videoReadService;
	private final RecommendationService recommendationService;
	private final CommentReadService commentReadService;
	private final ViewingHistoryWriteService viewingHistoryWriteService;

	@Transactional
	public void uploadVideo(AuthMember authMember, UploadVideoRequest uploadVideoRequest) {
		videoValidator.validateUploadVideoRequest(uploadVideoRequest);
		videoWriteService.uploadVideo(authMember, uploadVideoRequest);
	}

	public PagedResponse<VideoResponse> getRecommendedVideosForUser(AuthMember authMember) {
		final List<Video> videos = recommendationService.getRecommendedVideos(authMember.id());
		final List<VideoResponse> videoResponses = VideoMapper.toVideoListResponse(videos);
		final PaginationInfo paginationInfo = new PaginationInfo(1, false);
		return new PagedResponse<>(videoResponses, paginationInfo);
	}

	public PagedResponse<VideoResponse> getSearchVideos(String keyword, Pageable pageable) {
		final Page<Video> videosPage = videoReadService.searchVideos(keyword, pageable);

		final List<VideoResponse> videoResponses = videosPage.getContent().stream()
			.map(VideoMapper::toVideoResponse)
			.toList();

		final PaginationInfo paginationInfo = new PaginationInfo(videosPage.getNumber() + 1, videosPage.hasNext());
		return new PagedResponse<>(videoResponses, paginationInfo);
	}

	@Transactional
	public VideoDetailResponse getVideoDetail(Long videoId, AuthMember authMember) {
		videoWriteService.incrementViewCount(videoId);

		Optional.ofNullable(authMember)
			.ifPresent(member -> viewingHistoryWriteService.saveViewingHistory(member.id(), videoId));

		final Video video = videoReadService.getVideoById(videoId);
		final List<Comment> comments = commentReadService.getCommentsByVideoId(videoId);
		final List<CommentResponse> commentResponses = CommentMapper.toCommentListResponse(comments);

		return VideoMapper.toVideoDetailResponse(video, commentResponses);
	}

	@Transactional
	public void updateVideo(Long videoId, AuthMember authMember, UpdateVideoRequest updateVideoRequest) {
		final Video video = videoReadService.getVideoById(videoId);
		validateVideoOwner(video.getMember().getId(), authMember.id());
		videoWriteService.updateVideo(video, updateVideoRequest);
	}

	@Transactional
	public void deleteVideo(Long videoId, AuthMember authMember) {
		final Video video = videoReadService.getVideoById(videoId);
		validateVideoOwner(video.getMember().getId(), authMember.id());
		videoWriteService.deleteVideo(videoId);
	}

	private void validateVideoOwner(Long videoOwnerId, Long authMemberId) {
		if (!videoOwnerId.equals(authMemberId)) {
			throw new UnauthorizedException(ErrorMessage.FORBIDDEN_VIDEO_MODIFY);
		}
	}
}
