package com.vidcentral.api.application.comment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vidcentral.api.domain.comment.entity.Comment;
import com.vidcentral.api.domain.comment.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentReadService {

	private final CommentRepository commentRepository;

	public List<Comment> getCommentsByVideoId(Long videoId) {
		return commentRepository.findByVideoId(videoId);
	}
}
