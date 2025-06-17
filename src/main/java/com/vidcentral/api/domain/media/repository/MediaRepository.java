package com.vidcentral.api.domain.media.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vidcentral.api.domain.media.entity.Media;

public interface MediaRepository extends JpaRepository<Media, Long> {
}
