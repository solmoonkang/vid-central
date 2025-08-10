package com.vidcentral.api.domain.history.entity;

import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.global.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(
	name = "VIEWING_HISTORIES", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"member_id", "video_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ViewingHistory extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "history_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "video_id", nullable = false)
	private Video video;

	@Column(name = "last_watched_time")
	private Long lastWatchedTime;

	@Column(name = "is_finished")
	private Boolean isFinished;

	private ViewingHistory(Member member, Video video, Long lastWatchedTime, Boolean isFinished) {
		this.member = member;
		this.video = video;
		this.lastWatchedTime = lastWatchedTime;
		this.isFinished = isFinished;
	}
}
