package com.vidcentral.api.domain.reaction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vidcentral.api.domain.reaction.entity.VideoReaction;

public interface VideoReactionRepository extends JpaRepository<VideoReaction, Long> {

	@Query("SELECT vr FROM VideoReaction vr WHERE vr.member.id = :memberId AND vr.video.id = :videoId")
	Optional<VideoReaction> findByMemberIdAndVideoId(@Param("memberId") Long memberId, @Param("videoId") Long videoId);
}
