package com.vidcentral.global.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoConstant {

	// File Paths and Directories
	public static final String PROCESSED_VIDEO_PATH = "/home/user/processed/";
	public static final String THUMBNAIL_PATH = "/home/user/thumbnails";
	public static final String VIDEO_STORAGE_PATH = "/home/user/directory/";
	public static final String TEMP_DIRECTORY_NAME = "temp-chunks";

	// FFMPEG and Encoding Options
	public static final String FFMPEG_EXECUTABLE_PATH = "/user/bin/ffmpeg";
	public static final String[] VIDEO_ENCODING_OPTIONS = {
		"-c:v", "libx264", "-crf", "23", "-c:a", "aac", "-b:a", "128k"
	};
	public static final String[] THUMBNAIL_OPTIONS = {
		"-ss", "00:00:01", "-vframes", "1"
	};

	// Video Resoultion Values and Suffixes
	public static final String RESOLUTION_480P_VALUE = "480p";
	public static final String RESOLUTION_720P_VALUE = "720p";
	public static final String RESOLUTION_1080P_VALUE = "1080p";

	public static final String RESOLUTION_480P_SUFFIX = "_480p.mp4";
	public static final String RESOLUTION_720P_SUFFIX = "_720p.mp4";
	public static final String RESOLUTION_1080P_SUFFIX = "_1080p.mp4";

	// File and Chunk-related
	public static final String CHUNK_FILE_EXTENSION = ".part";
	public static final String DEFAULT_VIDEO_EXTENSION = ".mp4";
	public static final String THUMBNAIL_EXTENSION = ".jpg";
	public static final char FILE_EXTENSION_DELIMITER = '.';
}
