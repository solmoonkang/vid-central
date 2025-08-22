package com.vidcentral.api.application.video;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.comment.CommentReadService;
import com.vidcentral.api.application.comment.mapper.CommentMapper;
import com.vidcentral.api.application.history.ViewingHistoryWriteService;
import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.application.recommend.RecommendationService;
import com.vidcentral.api.application.video.mapper.VideoMapper;
import com.vidcentral.api.domain.comment.entity.Comment;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.domain.video.repository.VideoUploadRedisRepository;
import com.vidcentral.api.dto.comment.response.CommentResponse;
import com.vidcentral.api.dto.pagination.PagedResponse;
import com.vidcentral.api.dto.pagination.PaginationInfo;
import com.vidcentral.api.dto.video.request.CreateVideoRequest;
import com.vidcentral.api.dto.video.request.UpdateVideoRequest;
import com.vidcentral.api.dto.video.request.UploadChunkRequest;
import com.vidcentral.api.dto.video.response.VideoDetailResponse;
import com.vidcentral.api.dto.video.response.VideoResponse;
import com.vidcentral.global.auth.model.AuthMember;
import com.vidcentral.global.error.exception.BadRequestException;
import com.vidcentral.global.error.exception.UnauthorizedException;
import com.vidcentral.global.error.model.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoService {

	private final VideoWriteService videoWriteService;
	private final VideoReadService videoReadService;
	private final RecommendationService recommendationService;
	private final CommentReadService commentReadService;
	private final ViewingHistoryWriteService viewingHistoryWriteService;
	private final MemberReadService memberReadService;
	private final VideoUploadRedisRepository videoUploadRedisRepository;

	@Transactional
	public Long saveVideo(AuthMember authMember, CreateVideoRequest createVideoRequest) {
		final Member member = memberReadService.findMemberById(authMember.id());
		return videoWriteService.saveVideoMetadata(member, createVideoRequest).getId();
	}

	@Transactional
	public void uploadVideoChunk(Long videoId, AuthMember authMember, UploadChunkRequest uploadChunkRequest) {
		final Video video = videoReadService.findVideoById(videoId);
		validateVideoOwner(video.getMember().getId(), authMember.id());

		final Optional<Integer> storedTotalChunks = videoUploadRedisRepository.getTotalChunks(videoId);
		validateChunkIntegrity(uploadChunkRequest.chunkNumber(), uploadChunkRequest.totalChunks());
		validateTotalChunks(storedTotalChunks, uploadChunkRequest.totalChunks());
		validateChunkNotAlreadyUploaded(videoId, uploadChunkRequest.chunkNumber());

		videoWriteService.uploadVideoChunk(video, uploadChunkRequest);
	}

	public PagedResponse<VideoResponse> getRecommendedVideosForUser(AuthMember authMember) {
		final List<Video> videos = recommendationService.getRecommendedVideos(authMember.id());
		final List<VideoResponse> videoResponses = VideoMapper.toVideoListResponse(videos);
		final PaginationInfo paginationInfo = new PaginationInfo(1, false);
		return new PagedResponse<>(videoResponses, paginationInfo);
	}

	public PagedResponse<VideoResponse> getSearchVideos(String keyword, Pageable pageable) {
		final Page<VideoResponse> videoResponses = videoReadService.searchVideos(keyword, pageable);
		final PaginationInfo paginationInfo = new PaginationInfo(
			videoResponses.getNumber() + 1, videoResponses.hasNext());
		return new PagedResponse<>(videoResponses.getContent(), paginationInfo);
	}

	@Transactional
	@Cacheable(value = "videoDetail", key = "#videoId")
	public VideoDetailResponse getVideoDetail(Long videoId, AuthMember authMember) {
		videoWriteService.incrementViewCount(videoId);

		saveViewingHistory(authMember, videoId);

		final VideoDetailResponse videoDetailResponse = videoReadService.findVideoDetailResponseById(videoId);
		final List<Comment> comments = commentReadService.findCommentsByVideoId(videoId);
		final List<CommentResponse> commentResponses = CommentMapper.toCommentListResponse(comments);

		return videoDetailResponse.withComments(commentResponses);
	}

	@Transactional
	@CacheEvict(value = "videoDetail", key = "#videoId")
	public void updateVideo(Long videoId, AuthMember authMember, UpdateVideoRequest updateVideoRequest) {
		final Video video = videoReadService.findVideoById(videoId);
		validateVideoOwner(video.getMember().getId(), authMember.id());
		videoWriteService.updateVideo(video, updateVideoRequest);
	}

	@Transactional
	@CacheEvict(value = "videoDetail", key = "#videoId")
	public void deleteVideo(Long videoId, AuthMember authMember) {
		final Video video = videoReadService.findVideoById(videoId);
		validateVideoOwner(video.getMember().getId(), authMember.id());
		videoWriteService.deleteVideo(videoId);
	}

	private void saveViewingHistory(AuthMember authMember, Long videoId) {
		Optional.ofNullable(authMember)
			.ifPresent(member -> viewingHistoryWriteService.saveViewingHistory(member.id(), videoId));
	}

	private void validateVideoOwner(Long videoOwnerId, Long authMemberId) {
		if (!videoOwnerId.equals(authMemberId)) {
			throw new UnauthorizedException(ErrorCode.FORBIDDEN_VIDEO_MODIFY);
		}
	}

	private void validateChunkIntegrity(int chunkNumber, int totalChunks) {
		if (chunkNumber > totalChunks) {
			throw new BadRequestException(ErrorCode.INVALID_CHUNK_NUMBER_RANGE);
		}
	}

	private void validateTotalChunks(Optional<Integer> totalChunksOfFirstRequest, int totalChunksOfCurrentRequest) {
		if (totalChunksOfFirstRequest.isPresent() && totalChunksOfFirstRequest.get() != totalChunksOfCurrentRequest) {
			throw new BadRequestException(ErrorCode.TOTAL_CHUNKS_MISMATCH);
		}
	}

	private void validateChunkNotAlreadyUploaded(Long videoId, int chunkNumber) {
		if (videoUploadRedisRepository.hasChunk(videoId, chunkNumber)) {
			throw new BadRequestException(ErrorCode.DUPLICATE_CHUNK_UPLOAD);
		}
	}
}
