package com.vidcentral.global.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoConstant {

	public static final String PROCESSED_VIDEO_PATH = "/home/user/processed/";
	public static final String THUMBNAIL_PATH = "/home/user/thumbnails";
	public static final String VIDEO_STORAGE_PATH = "/home/user/directory/";
	public static final String TEMP_DIRECTORY_NAME = "temp-chunks";
	public static final String FFMPEG_EXECUTABLE_PATH = "/user/bin/ffmpeg";

	public static final String VIDEO_CODEC = "libx264";
	public static final String AUDIO_CODEC = "aac";
	public static final String AUDIO_BITRATE = "128k";

	public static final String CHUNK_FILE_EXTENSION = ".part";
	public static final String DEFAULT_VIDEO_EXTENSION = ".mp4";
	public static final String THUMBNAIL_EXTENSION = ".jpg";
	public static final String MASTER_M3U8_FILENAME = "master.m3u8";
	public static final char FILE_EXTENSION_DELIMITER = '.';
}
