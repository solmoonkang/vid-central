package com.vidcentral.api.domain.video.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
