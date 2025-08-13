package com.vidcentral.api.application.comment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vidcentral.api.domain.comment.entity.Comment;
import com.vidcentral.api.domain.comment.repository.CommentRepository;
import com.vidcentral.global.error.exception.NotFoundException;
import com.vidcentral.global.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentReadService {

	private final CommentRepository commentRepository;

	public List<Comment> findCommentsByVideoId(Long videoId) {
		return commentRepository.findByVideoId(videoId);
	}

	public Comment findCommentBy(Long memberId, Long videoId) {
		return commentRepository.findByMemberIdAndVideoId(memberId, videoId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND));
	}
}
