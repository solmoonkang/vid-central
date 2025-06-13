package com.vidcentral.api.domain.video.entity;

import static java.util.Objects.*;

import com.vidcentral.api.domain.member.entity.Member;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "VIDEOS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "video_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "video_url", nullable = false)
	private String videoURL;

	@Column(name = "thumbnail_url", nullable = false)
	private String thumbnailURL;

	private Video(Member member, String title, String description, String videoURL, String thumbnailURL) {
		this.member = member;
		this.title = title;
		this.description = description;
		this.videoURL = videoURL;
		this.thumbnailURL = thumbnailURL;
	}

	public void updateTitle(String title) {
		this.title = requireNonNullElse(title, this.title);
	}

	public void updateDescription(String description) {
		this.description = requireNonNullElse(description, this.description);
	}

	public void updateVideoURL(String newVideoURL) {
		this.videoURL = requireNonNullElse(newVideoURL, this.videoURL);
	}
}
