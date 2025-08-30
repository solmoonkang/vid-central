package com.vidcentral.fixtures.video;

import static com.vidcentral.fixtures.FixtureConstant.*;

import com.vidcentral.api.domain.member.entity.Member;
import com.vidcentral.api.domain.video.entity.Video;

public class VideoFixture {

	public static Video createVideoMetadata(Member member) {
		return Video.createVideoMetadata(
			member, DEFAULT_TITLE, DEFAULT_DESCRIPTION, DEFAULT_RESOLUTIONS, DEAFULT_CATEGORY, DEFAULT_TAGS);
	}

	public static CreateVideoRequestBuilder createCreateVideoRequest() {
		return new CreateVideoRequestBuilder();
	}

	public static UploadChunkRequestBuilder createUploadChunkRequest() {
		return new UploadChunkRequestBuilder();
	}

	public static VideoResponseBuilder createVideoResponse() {
		return new VideoResponseBuilder();
	}
}
