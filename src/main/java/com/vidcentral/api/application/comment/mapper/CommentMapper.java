package com.vidcentral.api.application.comment.mapper;

import java.util.List;

import com.vidcentral.api.domain.comment.entity.Comment;
import com.vidcentral.api.dto.comment.response.CommentResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

	public static CommentResponse toCommentResponse(Comment comment) {
		return new CommentResponse(comment.getMember().getNickname(), comment.getContent());
	}

	public static List<CommentResponse> toCommentListResponse(List<Comment> comments) {
		return comments.stream()
			.map(CommentMapper::toCommentResponse)
			.toList();
	}
}
