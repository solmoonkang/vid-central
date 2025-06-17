package com.vidcentral.api.domain.media.entity;

import com.vidcentral.api.domain.media.model.MediaType;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "ATTACHMENTS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attachment_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "media_id", nullable = false)
	private Media media;

	@Enumerated(EnumType.STRING)
	@Column(name = "media_type", nullable = false, length = 10)
	private MediaType mediaType;

	@Column(name = "file_url", nullable = false)
	private String fileURL;

	@Column(name = "thumbnail_url")
	private String thumbnailURL;

	private Attachment(Media media, MediaType mediaType, String fileURL, String thumbnailURL) {
		this.media = media;
		this.mediaType = mediaType;
		this.fileURL = fileURL;
		this.thumbnailURL = thumbnailURL;
	}

	public static Attachment createImageAttachment(Media media, String imageURL) {
		return new Attachment(media, MediaType.IMAGE, imageURL, null);
	}

	public static Attachment createVideoAttachment(Media media, String videoURL, String thumbnailURL) {
		return new Attachment(media, MediaType.VIDEO, videoURL, thumbnailURL);
	}
}
