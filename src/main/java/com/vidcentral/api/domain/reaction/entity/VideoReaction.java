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

	public static VideoReaction createVideoReaction(Member member, Video video, ReactionType reactionType) {
		return new VideoReaction(member, video, reactionType);
	}

	public void updateReactionType(ReactionType reactionType) {
		this.reactionType = reactionType;
	}
}
