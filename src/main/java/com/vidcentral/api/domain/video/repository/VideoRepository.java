package com.vidcentral.api.domain.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vidcentral.api.domain.video.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long>, VideoRepositoryCustom {

	@Modifying
	@Query("UPDATE Video v SET v.viewCount = v.viewCount + 1 WHERE v.id = :videoId")
	void incrementViewCount(@Param("videoId") Long videoId);
}
