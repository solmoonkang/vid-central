package com.vidcentral.api.domain.media.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vidcentral.api.domain.media.entity.Media;
import com.vidcentral.api.domain.media.entity.MediaLike;
import com.vidcentral.api.domain.member.entity.Member;

public interface MediaLikeRepository extends JpaRepository<MediaLike, Long> {

	Optional<MediaLike> findByMediaAndMember(Media media, Member member);

	long countByMediaAndIsLike(Media media, boolean isLike);
}
