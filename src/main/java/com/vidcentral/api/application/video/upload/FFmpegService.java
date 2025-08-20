package com.vidcentral.api.application.video.upload;

import static com.vidcentral.global.common.util.VideoConstant.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.vidcentral.global.common.util.FFmpegOption;
import com.vidcentral.global.common.util.ResolutionOption;
import com.vidcentral.global.common.util.ThumbnailOption;
import com.vidcentral.global.error.exception.InternalServerException;
import com.vidcentral.global.error.model.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FFmpegService {

	public String transcodeVideoToHLS(String inputFilePath, List<String> resolutions, String baseFileName) {
		final String outputDirectory = PROCESSED_VIDEO_PATH + baseFileName;
		final List<String> command = buildHLSCommand(inputFilePath, outputDirectory, resolutions);
		executeCommand(command, ErrorCode.VIDEO_PROCESSING_FAILED);
		return Paths.get(outputDirectory, MASTER_M3U8_FILENAME).toString();
	}

	public String createThumbnail(String inputFilePath, String baseFileName) {
		final String thumbnailFilePath = THUMBNAIL_PATH + baseFileName + THUMBNAIL_EXTENSION;
		final List<String> commands = buildThumbnailCommand(inputFilePath, thumbnailFilePath);
		executeCommand(commands, ErrorCode.THUMBNAIL_CREATION_FAILED);
		return thumbnailFilePath;
	}

	private List<String> buildThumbnailCommand(String inputFilePath, String thumbnailFilePath) {
		final List<String> commands = new ArrayList<>();
		commands.add(FFMPEG_EXECUTABLE_PATH);
		commands.add(FFmpegOption.INPUT.getCommand());
		commands.add(inputFilePath);
		commands.add(ThumbnailOption.SEEK_POSITION.getCommand());
		commands.add("00:00:01");
		commands.add(ThumbnailOption.FRAMES_TO_EXTRACT.getCommand());
		commands.add("1");
		commands.add(thumbnailFilePath);
		return commands;
	}

	private List<String> buildHLSCommand(String inputFilePath, String outputDirectory, List<String> resolutions) {
		final List<String> commands = new ArrayList<>();
		commands.add(FFMPEG_EXECUTABLE_PATH);
		commands.add(FFmpegOption.OVERWRITE_OUTPUT.getCommand());
		commands.add(FFmpegOption.INPUT.getCommand());
		commands.add(inputFilePath);

		resolutions.forEach(resolutionValue -> {
			ResolutionOption resolution = ResolutionOption.fromValue(resolutionValue);
			addResolutionCommands(commands, resolution, outputDirectory);
		});

		addStreamMapCommands(commands, resolutions);
		return commands;
	}

	private void addResolutionCommands(List<String> commands, ResolutionOption resolution, String outputDirectory) {
		final String hlsSegmentPath = Paths.get(outputDirectory, resolution + "_%03d.ts").toString();
		final String hlsPlaylistPath = Paths.get(outputDirectory, resolution + ".m3u8").toString();

		commands.addAll(Arrays.asList(
			"-vf", "scale=-2:" + resolution.getHeight(),
			FFmpegOption.VIDEO_CODEC.getCommand(), VIDEO_CODEC,
			FFmpegOption.AUDIO_CODEC.getCommand(), AUDIO_CODEC,
			FFmpegOption.AUDIO_BITRATE.getCommand(), AUDIO_BITRATE,
			FFmpegOption.HLS_TIME.getCommand(), "10",
			FFmpegOption.HLS_PLAYLIST_TYPE.getCommand(), "vod",
			FFmpegOption.HLS_SEGMENT_FILENAME.getCommand(), hlsSegmentPath,
			FFmpegOption.HLS_FORMAT.getCommand(), "hls",
			hlsPlaylistPath
		));
	}

	private void addStreamMapCommands(List<String> commands, List<String> resolutions) {
		commands.add(FFmpegOption.STREAM_MAP.getCommand());
		commands.add(buildStreamMap(resolutions));
		commands.add(FFmpegOption.MASTER_PLAYLIST.getCommand());
		commands.add(MASTER_M3U8_FILENAME);
	}

	private String buildStreamMap(List<String> resolutions) {
		return resolutions.stream()
			.map(resolution -> "v:" + resolutions.indexOf(resolution) + ",a:" + resolutions.indexOf(resolution))
			.collect(Collectors.joining(" "));
	}

	private void executeCommand(List<String> command, ErrorCode errorCode) {
		try {
			final ProcessBuilder processBuilder = new ProcessBuilder(command).inheritIO();
			final Process process = processBuilder.start();

			final int exitCode = process.waitFor();
			if (exitCode != 0)
				throw new InternalServerException(errorCode);
		} catch (IOException | InterruptedException e) {
			throw new InternalServerException(errorCode);
		}
	}
}
