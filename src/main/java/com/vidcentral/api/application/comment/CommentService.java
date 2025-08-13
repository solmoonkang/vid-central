package com.vidcentral.api.application.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.application.video.VideoReadService;
import com.vidcentral.api.domain.comment.entity.Comment;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.dto.comment.request.CreateCommentRequest;
import com.vidcentral.api.dto.comment.request.UpdateCommentRequest;
import com.vidcentral.global.auth.model.AuthMember;
import com.vidcentral.global.error.exception.UnauthorizedException;
import com.vidcentral.global.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final MemberReadService memberReadService;
	private final VideoReadService videoReadService;
	private final CommentWriteService commentWriteService;
	private final CommentReadService commentReadService;

	@Transactional
	public void saveComment(Long videoId, AuthMember authMember, CreateCommentRequest createCommentRequest) {
		final Member member = memberReadService.findMemberById(authMember.id());
		final Video video = videoReadService.findVideoById(videoId);
		commentWriteService.saveComment(member, video, createCommentRequest);
	}

	@Transactional
	public void updateComment(Long videoId, AuthMember authMember, UpdateCommentRequest updateCommentRequest) {
		final Video video = videoReadService.findVideoById(videoId);
		validateCommentOwner(video.getMember().getId(), authMember.id());

		final Member member = memberReadService.findMemberById(authMember.id());
		final Comment comment = commentReadService.findCommentBy(member.getId(), video.getId());
		commentWriteService.updateComment(comment, updateCommentRequest);
	}

	private void validateCommentOwner(Long commentOwnerId, Long authMemberId) {
		if (!commentOwnerId.equals(authMemberId)) {
			throw new UnauthorizedException(ErrorMessage.FORBIDDEN_COMMENT_MODIFY);
		}
	}
}
