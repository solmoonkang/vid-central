package com.vidcentral.api.domain.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vidcentral.api.domain.video.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
