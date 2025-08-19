package com.vidcentral.global.common.util;

import com.vidcentral.global.error.exception.BadRequestException;
import com.vidcentral.global.error.model.ErrorCode;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoConstant {

	public static final String PROCESSED_VIDEO_PATH = "/home/user/processed/";
	public static final String THUMBNAIL_PATH = "/home/user/thumbnails";
	public static final String VIDEO_STORAGE_PATH = "/home/user/directory/";
	public static final String TEMP_DIRECTORY_NAME = "temp-chunks";
	public static final String FFMPEG_EXECUTABLE_PATH = "/user/bin/ffmpeg";

	public static final String[] INPUT_OPTIONS = {"-i"};
	public static final String[] GLOBAL_OPTIONS = {"-y"};
	public static final String[] THUMBNAIL_OPTIONS = {"-ss", "00:00:01", "-vframes", "1"};
	public static final String[] VIDEO_STREAM_MAP_OPTIONS = {"-map", "0:v", "-map", "0:a?"};

	public static final String VIDEO_CODEC = "libx264";
	public static final String AUDIO_CODEC = "aac";
	public static final String AUDIO_BITRATE = "128k";
	public static final String HLS_TIME = "10";
	public static final String HLS_PLAYLIST_TYPE = "vod";
	public static final String HLS_FORMAT = "hls";

	public static final String RESOLUTION_480P_VALUE = "480p";
	public static final String RESOLUTION_720P_VALUE = "720p";
	public static final String RESOLUTION_1080P_VALUE = "1080p";

	public static final int RESOLUTION_480P_HEIGHT = 480;
	public static final int RESOLUTION_720P_HEIGHT = 720;
	public static final int RESOLUTION_1080P_HEIGHT = 1080;

	public static final String CHUNK_FILE_EXTENSION = ".part";
	public static final String DEFAULT_VIDEO_EXTENSION = ".mp4";
	public static final String THUMBNAIL_EXTENSION = ".jpg";
	public static final String MASTER_M3U8_FILENAME = "master.m3u8";
	public static final char FILE_EXTENSION_DELIMITER = '.';

	public static int getHeight(String resolution) {
		return switch (resolution) {
			case RESOLUTION_480P_VALUE -> RESOLUTION_480P_HEIGHT;
			case RESOLUTION_720P_VALUE -> RESOLUTION_720P_HEIGHT;
			case RESOLUTION_1080P_VALUE -> RESOLUTION_1080P_HEIGHT;
			default -> throw new BadRequestException(ErrorCode.INVALID_RESOLUTION, resolution);
		};
	}
}
