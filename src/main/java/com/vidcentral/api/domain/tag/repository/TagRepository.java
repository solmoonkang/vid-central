package com.vidcentral.api.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vidcentral.api.domain.tag.entity.Tag;

public interface TagRepository extends JpaRepository<Long, Tag> {
}
