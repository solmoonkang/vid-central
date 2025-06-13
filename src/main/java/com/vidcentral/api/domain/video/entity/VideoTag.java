package com.vidcentral.api.domain.video.entity;

import com.vidcentral.api.domain.tag.entity.Tag;

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
@Table(name = "VIDEO_TAGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoTag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "video_tag_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "video_id", nullable = false)
	private Video video;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id", nullable = false)
	private Tag tag;

	private VideoTag(Video video, Tag tag) {
		this.video = video;
		this.tag = tag;
	}
}
