package com.vidcentral.api.domain.reaction.entity;

import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.reaction.model.ReactionType;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.global.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "VIDEO_REACTIONS", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"member_id", "video_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoReaction extends BaseTimeEntity {

	// 좋아요와 싫어요의 총 개수를 자주 조회해야 하는 경우, 매번 VIDEO_REACTIONS 테이블을 집계하는 것은 성능 부담이 될 수 있다.
	// Video 엔티티에 likeCount와 dislikeCount 필드를 추가하고, VideoReaction이 저장되거나 수정될 때마다 이 카운트 필드를 업데이트하는 카운터 캐싱 방식을 고려할 수 있다.
	// 이때 낙관적 락을 사용하면 동시성 문제를 안전하게 처리할 수 있다.

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reaction_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "video_id", nullable = false)
	private Video video;

	@Enumerated(EnumType.STRING)
	@Column(name = "reaction_type")
	private ReactionType reactionType;

	private VideoReaction(Member member, Video video, ReactionType reactionType) {
		this.member = member;
		this.video = video;
		this.reactionType = reactionType;
	}
}
