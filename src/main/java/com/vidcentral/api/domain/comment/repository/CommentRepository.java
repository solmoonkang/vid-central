package com.vidcentral.api.domain.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vidcentral.api.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByVideoId(Long videoId);
}
