package com.vidcentral.global.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FFmpegOption {

	INPUT("-i"),
	OVERWRITE_OUTPUT("-y"),
	VIDEO_CODEC("-c:v"),
	AUDIO_CODEC("-c:a"),
	AUDIO_BITRATE("-b:a"),
	HLS_FORMAT("-f"),
	HLS_TIME("-hls_time"),
	HLS_PLAYLIST_TYPE("-hls_playlist_type"),
	HLS_SEGMENT_FILENAME("-hls_segment_filename"),
	MASTER_PLAYLIST("-master_pl_name"),
	STREAM_MAP("-var_stream_map");

	private final String command;
}
