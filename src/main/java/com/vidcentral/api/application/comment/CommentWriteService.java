package com.vidcentral.api.application.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.domain.comment.entity.Comment;
import com.vidcentral.api.domain.comment.repository.CommentRepository;
import com.vidcentral.api.domain.media.entity.Media;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.dto.comment.request.CreateCommentRequest;
import com.vidcentral.api.dto.comment.request.UpdateCommentRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentWriteService {

	private final CommentRepository commentRepository;

	public void createComment(Member member, Media media, CreateCommentRequest createCommentRequest) {
		final Comment comment = Comment.createComment(member, media, createCommentRequest);
		commentRepository.save(comment);
	}

	public void updateComment(Comment comment, UpdateCommentRequest updateCommentRequest) {
		comment.updateComment(updateCommentRequest);
	}

	public void deleteComment(Comment comment) {
		commentRepository.delete(comment);
	}
}
