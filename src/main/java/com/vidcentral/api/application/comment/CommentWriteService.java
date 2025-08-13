package com.vidcentral.api.application.comment;

import org.springframework.stereotype.Service;

import com.vidcentral.api.domain.comment.entity.Comment;
import com.vidcentral.api.domain.comment.repository.CommentRepository;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.dto.comment.request.CreateCommentRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentWriteService {

	private final CommentRepository commentRepository;

	public void saveComment(Member member, Video video, CreateCommentRequest createCommentRequest) {
		final Comment comment = Comment.createComment(member, video, createCommentRequest.content());
		commentRepository.save(comment);
	}
}
