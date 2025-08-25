package com.vidcentral.api.domain.video.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vidcentral.api.domain.member.entity.QMember;
import com.vidcentral.api.domain.video.entity.QVideo;
import com.vidcentral.api.domain.video.model.Category;
import com.vidcentral.api.dto.video.response.QVideoDetailResponse;
import com.vidcentral.api.dto.video.response.VideoDetailResponse;
import com.vidcentral.api.dto.video.response.VideoResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VideoQueryRepository implements VideoRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	private final QVideo video = QVideo.video;
	private final QMember member = QMember.member;

	@Override
	public List<VideoResponse> findRecommendedVideoResponsesBy(List<Category> categories, List<String> tags,
		List<Long> excludedVideoIds, Pageable pageable) {
		final StringPath tagPath = video.tags.any();

		return jpaQueryFactory
			.select(toVideoResponseProjection())
			.from(video)
			.join(video.member, member)
			.where(video.category.in(categories).or(tagPath.in(tags)).and(video.id.notIn(excludedVideoIds)))
			.distinct()
			.orderBy(video.viewCount.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public Page<VideoResponse> findVideoResponseByKeyword(String keyword, Pageable pageable) {
		final List<VideoResponse> videoResponses = jpaQueryFactory
			.select(toVideoResponseProjection())
			.from(video)
			.join(video.member, member)
			.where(video.title.contains(keyword))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		final Long count = jpaQueryFactory
			.select(video.count())
			.from(video)
			.join(video.member, member)
			.where(video.title.contains(keyword).or(member.nickname.contains(keyword)))
			.fetchOne();

		return PageableExecutionUtils.getPage(videoResponses, pageable, () -> count);
	}

	private ConstructorExpression<VideoResponse> toVideoResponseProjection() {
		return Projections.constructor(VideoResponse.class, video.id, video.thumbnailPath, video.title, member.nickname,
			video.viewCount, video.createdAt);
	}

	@Override
	public Optional<VideoDetailResponse> findVideoDetailWithUploader(Long videoId) {
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
