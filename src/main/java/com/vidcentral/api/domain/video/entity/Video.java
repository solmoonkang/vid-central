package com.vidcentral.api.domain.video.entity;

import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.model.UploadStatus;
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
	@Column(name = "video_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "file_path")
	private String filePath;

	@Column(name = "thumbnail_path")
	private String thumbnailPath;

	@Column(name = "upload_status")
	private UploadStatus uploadStatus;

	@Column(name = "view_count")
	private Long viewCount;

	public Video(Member member, String title, String description, String filePath, String thumbnailPath) {
		this.member = member;
		this.title = title;
		this.description = description;
		this.filePath = filePath;
		this.thumbnailPath = thumbnailPath;
		this.uploadStatus = UploadStatus.UPLOADING;
		this.viewCount = 0L;
	}

	public static Video createVideo(Member member, String title, String description) {
		return new Video(member, title, description, null, null);
	}

	public void updateUploadStatus(UploadStatus uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public void updateFilePathAndThumbnailPath(String filePath, String thumbnailPath) {
		this.filePath = filePath;
		this.thumbnailPath = thumbnailPath;
	}
}
