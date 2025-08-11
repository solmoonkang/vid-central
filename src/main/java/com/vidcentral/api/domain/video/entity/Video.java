package com.vidcentral.api.domain.video.entity;

import java.util.ArrayList;
import java.util.List;

import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.model.Category;
import com.vidcentral.api.domain.video.model.UploadStatus;
import com.vidcentral.global.common.entity.BaseTimeEntity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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

	@Column(name = "original_file_name")
	private String originalFileName;

	@Column(name = "file_path")
	private String filePath;

	@Column(name = "thumbnail_path")
	private String thumbnailPath;

	@Column(name = "upload_status")
	private UploadStatus uploadStatus;

	@Column(name = "view_count")
	private Long viewCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false)
	private Category category;

	@ElementCollection
	@CollectionTable(
		name = "VIDEO_TAGS", joinColumns = @JoinColumn(name = "video_id")
	)
	@Column(name = "tag")
	private List<String> tags = new ArrayList<>();

	@Column(name = "like_count", nullable = false)
	private Long likeCount;

	@Column(name = "dislike_count", nullable = false)
	private Long dislikeCount;

	public Video(Member member, String title, String description, String originalFileName, String filePath,
		String thumbnailPath, Category category, List<String> tags) {

		this.member = member;
		this.title = title;
		this.description = description;
		this.originalFileName = originalFileName;
		this.filePath = filePath;
		this.thumbnailPath = thumbnailPath;
		this.uploadStatus = UploadStatus.UPLOADING;
		this.viewCount = 0L;
		this.category = category;
		this.tags = tags;
		this.likeCount = 0L;
		this.dislikeCount = 0L;
	}

	public static Video createVideo(Member member, String title, String description, String originalFileName,
		Category category, List<String> tags) {

		return new Video(member, title, description, originalFileName, null, null, category, tags);
	}

	public void updateUploadStatus(UploadStatus uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public void updateOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public void updateFilePath(List<String> filePaths) {
		this.filePath = String.join(",", filePaths);
	}

	public void updateThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}
}
