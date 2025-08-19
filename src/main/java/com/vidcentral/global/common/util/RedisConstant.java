package com.vidcentral.global.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisConstant {

	public static final String VIDEO_CHUNK_PREFIX = "video:chunks:";
	public static final String TOTAL_CHUNKS_PREFIX = ":totalChunks";
}
