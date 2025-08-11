package com.vidcentral.api.domain.video.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.api.domain.video.model.Category;

public interface VideoRepository extends JpaRepository<Video, Long> {

	@Query("SELECT DISTINCT v FROM Video v "
		+ "LEFT JOIN FETCH v.tags vt "
		+ "WHERE (v.category IN :categories OR vt IN :tags) "
		+ "AND v.id NOT IN :excludedVideoIds "
		+ "ORDER BY v.viewCount DESC")
	List<Video> findRecommendedVideos(
		@Param("categories") List<Category> categories,
		@Param("tags") List<String> tags,
		@Param("excludedVideoIds") List<Long> excludedVideoIds,
		Pageable pageable);

	@Query("SELECT v FROM Video v JOIN FETCH v.member m WHERE v.title LIKE %:keyword% OR m.nickname LIKE %:keyword%")
	Page<Video> findByTitleOrNicknameContaining(@Param("keyword") String keyword, Pageable pageable);

	@Modifying
	@Query("UPDATE Video v SET v.viewCount = v.viewCount + 1 WHERE v.id = :videoId")
	void incrementViewCount(@Param("videoId") Long videoId);
}
