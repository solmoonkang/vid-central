package com.vidcentral.fixtures.video;

import static com.vidcentral.fixtures.FixtureConstant.*;

import java.time.LocalDateTime;

import com.vidcentral.api.dto.video.response.VideoResponse;

public class VideoResponseBuilder {

	private Long videoId = DEFAULT_VIDEO_ID;
	private String thumbnailPath = DEFAULT_THUMBNAIL_PATH;
	private String title = DEFAULT_TITLE;
	private String uploaderNickname = DEFAULT_NICKNAME;
	private Long viewCount = DEFAULT_VIEW_COUNT;
	private LocalDateTime uploadedAt = DEFAULT_UPLOADED_AT;

	public VideoResponseBuilder videoId(Long videoId) {
		this.videoId = videoId;
		return this;
	}

	public VideoResponseBuilder thumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
		return this;
	}

	public VideoResponseBuilder title(String title) {
		this.title = title;
		return this;
	}

	public VideoResponseBuilder uploaderNickname(String uploaderNickname) {
		this.uploaderNickname = uploaderNickname;
		return this;
	}

	public VideoResponseBuilder viewCount(Long viewCount) {
		this.viewCount = viewCount;
		return this;
	}

	public VideoResponseBuilder uploadedAt(LocalDateTime uploadedAt) {
		this.uploadedAt = uploadedAt;
		return this;
	}

	public VideoResponse build() {
		return new VideoResponse(videoId, thumbnailPath, title, uploaderNickname, viewCount, uploadedAt);
	}
}
