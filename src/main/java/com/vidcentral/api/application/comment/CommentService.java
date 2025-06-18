package com.vidcentral.api.application.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.application.media.MediaReadService;
import com.vidcentral.api.application.member.MemberReadService;
import com.vidcentral.api.domain.auth.entity.AuthMember;
import com.vidcentral.api.domain.comment.entity.Comment;
import com.vidcentral.api.domain.comment.repository.CommentRepository;
import com.vidcentral.api.domain.media.entity.Media;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.comment.request.CreateCommentRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

	private final MemberReadService memberReadService;
	private final MediaReadService mediaReadService;
	private final CommentRepository commentRepository;

	@Transactional
	public void createComment(AuthMember authMember, Long mediaId, CreateCommentRequest createCommentRequest) {
		final Member member = memberReadService.findMember(authMember.email());
		final Media media = mediaReadService.readMedia(mediaId);
		final Comment comment = Comment.createComment(member, media, createCommentRequest);
		commentRepository.save(comment);
	}
}
