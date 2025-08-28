package com.vidcentral.fixtures.video;

import static com.vidcentral.fixtures.FixtureConstant.*;

import org.springframework.web.multipart.MultipartFile;

import com.vidcentral.api.dto.video.request.UploadChunkRequest;

public class UploadChunkRequestBuilder {

	private MultipartFile videoFile = DEAFULT_VIDEO_FILE;
	private String originalFileName = DEFAULT_ORIGINAL_FILE_NAME;
	private int chunkNumber = DEFAULT_CHUNK_NUMBER;
	private int totalChunks = DEFAULT_TOTAL_CHUNKS;

	public UploadChunkRequestBuilder videoFile(MultipartFile videoFile) {
		this.videoFile = videoFile;
		return this;
	}

	public UploadChunkRequestBuilder originalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
		return this;
	}

	public UploadChunkRequestBuilder chunkNumber(int chunkNumber) {
		this.chunkNumber = chunkNumber;
		return this;
	}

	public UploadChunkRequestBuilder totalChunks(int totalChunks) {
		this.totalChunks = totalChunks;
		return this;
	}

	public UploadChunkRequest build() {
		return new UploadChunkRequest(videoFile, originalFileName, chunkNumber, totalChunks);
	}
}
