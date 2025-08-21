package com.vidcentral.api.domain.video.repository;

import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vidcentral.api.domain.member.entity.QMember;
import com.vidcentral.api.domain.video.entity.QVideo;
import com.vidcentral.api.dto.video.response.QVideoDetailResponse;
import com.vidcentral.api.dto.video.response.VideoDetailResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VideoQueryRepository implements VideoRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<VideoDetailResponse> findVideoDetailWithUploader(Long videoId) {
		final QVideo video = QVideo.video;
		final QMember member = QMember.member;

		return Optional.ofNullable(jpaQueryFactory
			.select(toVideoDetailResponseProjection(video, member))
			.from(video)
			.join(video.member, member)
			.where(video.id.eq(videoId))
			.fetchOne()
		);
	}

	private QVideoDetailResponse toVideoDetailResponseProjection(QVideo video, QMember member) {
		return new QVideoDetailResponse(video.id, video.title, video.description, video.createdAt, video.filePath,
			video.thumbnailPath, video.viewCount, member.nickname, video.likeCount, video.dislikeCount);
	}
}
