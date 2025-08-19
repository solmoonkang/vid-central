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
import jakarta.persistence.Version;
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

	@ElementCollection
	@CollectionTable(name = "VIDEO_RESOLUTIONS", joinColumns = {
		@JoinColumn(name = "video_id")
	})
	@Column(name = "resolutions")
	private List<String> resolutions = new ArrayList<>();

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

	@Version
	private long version;

	public Video(Member member, String title, String description, String originalFileName, String filePath,
		String thumbnailPath, List<String> resolutions, Category category, List<String> tags) {

		this.member = member;
		this.title = title;
		this.description = description;
		this.originalFileName = originalFileName;
		this.filePath = filePath;
		this.thumbnailPath = thumbnailPath;
		this.uploadStatus = UploadStatus.UPLOADING;
		this.viewCount = 0L;
		this.resolutions = resolutions;
		this.category = category;
		this.tags = tags;
		this.likeCount = 0L;
		this.dislikeCount = 0L;
	}

	public static Video createVideoMetadata(Member member, String title, String description, List<String> resolutions,
		Category category, List<String> tags) {

		return new Video(member, title, description, null, null, null, resolutions, category, tags);
	}

	public void updateVideoFileInfo(String originalFileName, String filePath, UploadStatus uploadStatus) {

		this.originalFileName = originalFileName;
		this.filePath = filePath;
		this.uploadStatus = uploadStatus;
	}

	public void updateUploadStatus(UploadStatus uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public void updateFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void updateThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public void updateVideo(String title, String description, String thumbnailPath, Category categories,
		List<String> tags) {

		this.title = title;
		this.description = description;
		this.thumbnailPath = thumbnailPath;
		this.category = categories;
		this.tags = tags;
	}

	public void incrementLikeCount() {
		this.likeCount++;
	}

	public void decrementLikeCount() {
		this.likeCount--;
	}

	public void incrementDislikeCount() {
		this.dislikeCount++;
	}

	public void decrementDislikeCount() {
		this.dislikeCount--;
	}
}
