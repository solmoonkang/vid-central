package com.vidcentral.api.domain.history.entity;

import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.entity.Video;
import com.vidcentral.global.common.entity.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "VIEW_HISTORIES")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ViewHistory extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "video_id", nullable = false)
	private Video video;

	private ViewHistory(Member member, Video video) {
		this.member = member;
		this.video = video;
	}
}
