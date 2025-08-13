package com.vidcentral.api.domain.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vidcentral.api.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByVideoId(Long videoId);

	@Query("SELECT c FROM Comment c WHERE c.member.id = :memberId AND c.video.id = :videoId")
	Optional<Comment> findByMemberIdAndVideoId(@Param("memberId") Long memberId, @Param("videoId") Long videoId);
}
