package com.vidcentral.api.domain.media.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vidcentral.api.domain.media.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
