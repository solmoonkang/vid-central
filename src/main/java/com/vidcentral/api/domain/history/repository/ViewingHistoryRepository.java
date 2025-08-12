package com.vidcentral.api.domain.history.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vidcentral.api.domain.history.entity.ViewingHistory;

public interface ViewingHistoryRepository extends JpaRepository<ViewingHistory, Long> {

	@Query("SELECT vh FROM ViewingHistory vh "
		+ "JOIN  FETCH vh.video v "
		+ "WHERE vh.member.id = :memberId "
		+ "ORDER BY vh.createdAt DESC")
	List<ViewingHistory> findWithVideoByMemberId(@Param("memberId") Long memberId, Pageable pageable);

	@Query("SELECT vh FROM ViewingHistory vh WHERE vh.member.id = :memberId AND vh.video.id = :videoId")
	Optional<ViewingHistory> findByMemberIdAndVideoId(@Param("memberId") Long memberId, @Param("videoId") Long videoId);

	@Query("SELECT vh FROM ViewingHistory vh "
		+ "JOIN  FETCH vh.video v "
		+ "WHERE vh.member.id = :memberId "
		+ "ORDER BY vh.createdAt DESC")
	Page<ViewingHistory> findPagedWithVideoByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
