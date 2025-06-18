package com.vidcentral.api.application.comment;

import static com.vidcentral.global.error.model.ErrorMessage.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vidcentral.api.domain.comment.entity.Comment;
import com.vidcentral.api.domain.comment.repository.CommentRepository;
import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.global.error.exception.ForbiddenException;
import com.vidcentral.global.error.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentReadService {

	private final CommentRepository commentRepository;

	public Comment readComment(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new NotFoundException(FAILED_COMMENT_NOT_FOUND_ERROR));
	}

	public void validateUpdatePermission(Member member, Comment comment) {
		if (!comment.getMember().getId().equals(member.getId())) {
			throw new ForbiddenException(FAILED_FORBIDDEN_COMMENT_MODIFY_ERROR);
		}
	}

	public void validateDeletePermission(Member member, Comment comment) {
		final Long commentOwnerId = comment.getMember().getId();
		final Long mediaOwnerId = comment.getMedia().getMember().getId();

		if (!commentOwnerId.equals(member.getId()) && !mediaOwnerId.equals(member.getId())) {
			throw new ForbiddenException(FAILED_FORBIDDEN_COMMENT_DELETE_ERROR);
		}
	}
}
