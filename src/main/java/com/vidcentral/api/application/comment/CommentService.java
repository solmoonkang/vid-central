package com.vidcentral.api.application.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.media.MediaReadService;
import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.domain.comment.entity.Comment;
import com.vidcentral.api.domain.media.entity.Media;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.comment.request.CreateCommentRequest;
import com.vidcentral.api.dto.comment.request.UpdateCommentRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

	private final MemberReadService memberReadService;
	private final MediaReadService mediaReadService;
	private final CommentReadService commentReadService;
	private final CommentWriteService commentWriteService;

	@Transactional
	public void createComment(AuthMember authMember, Long mediaId, CreateCommentRequest createCommentRequest) {
		final Member member = memberReadService.findMember(authMember.email());
		final Media media = mediaReadService.readMedia(mediaId);
		commentWriteService.createComment(member, media, createCommentRequest);
	}

	@Transactional
	public void updateComment(AuthMember authMember, Long commentId, UpdateCommentRequest updateCommentRequest) {
		final Member member = memberReadService.findMember(authMember.email());
		final Comment comment = commentReadService.readComment(commentId);
		commentReadService.validateUpdatePermission(member, comment);
		commentWriteService.updateComment(comment, updateCommentRequest);
	}

	@Transactional
	public void deleteComment(AuthMember authMember, Long commentId) {
		final Member member = memberReadService.findMember(authMember.email());
		final Comment comment = commentReadService.readComment(commentId);
		commentReadService.validateDeletePermission(member, comment);
		commentWriteService.deleteComment(comment);
	}
}
