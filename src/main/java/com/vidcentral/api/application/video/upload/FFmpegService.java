package com.vidcentral.api.application.video.upload;

import static com.vidcentral.global.common.util.VideoConstant.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.vidcentral.global.error.exception.InternalServerException;
import com.vidcentral.global.error.model.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FFmpegService {

	public String processVideo(String inputFilePath, List<String> resolutions, String baseFileName) {
		final String outputDirectory = PROCESSED_VIDEO_PATH + baseFileName;
		final List<String> command = buildHLSCommand(inputFilePath, outputDirectory, resolutions);
		executeCommand(command, ErrorCode.VIDEO_PROCESSING_FAILED);
		return Paths.get(outputDirectory, MASTER_M3U8_FILENAME).toString();
	}

	public String createThumbnail(String inputFilePath, String baseFileName) {
		final String thumbnailFilePath = THUMBNAIL_PATH + baseFileName + THUMBNAIL_EXTENSION;
		final List<String> commands = buildCommand(inputFilePath, thumbnailFilePath);
		executeCommand(commands, ErrorCode.THUMBNAIL_CREATION_FAILED);
		return thumbnailFilePath;
	}

	private List<String> buildCommand(String inputFilePath, String outputFilePaths) {
		final List<String> commands = new ArrayList<>();
		commands.add(FFMPEG_EXECUTABLE_PATH);
		commands.addAll(Arrays.asList(INPUT_OPTIONS));
		commands.add(inputFilePath);
		commands.addAll(Arrays.asList(THUMBNAIL_OPTIONS));
		commands.add(outputFilePaths);

		return commands;
	}

	private List<String> buildHLSCommand(String inputFilePath, String outputDirectory, List<String> resolutions) {
		final List<String> commands = new ArrayList<>();
		commands.add(FFMPEG_EXECUTABLE_PATH);
		commands.addAll(Arrays.asList(GLOBAL_OPTIONS));
		commands.addAll(Arrays.asList(INPUT_OPTIONS));
		commands.add(inputFilePath);

		resolutions.forEach(resolution -> addResolutionCommands(commands, resolution, outputDirectory));
		addStreamMapCommands(commands, resolutions);

		return commands;
	}

	private void addResolutionCommands(List<String> commands, String resolution, String outputDirectory) {
		final String hlsSegmentPath = Paths.get(outputDirectory, resolution + "_%03d.ts").toString();
		final String hlsPlaylistPath = Paths.get(outputDirectory, resolution + ".m3u8").toString();

		commands.addAll(Arrays.asList(
			"-vf", "scale=-2:" + getHeight(resolution),
			"-c:v", VIDEO_CODEC, "-c:a", AUDIO_CODEC, "-b:a", AUDIO_BITRATE,
			"-hls_time", HLS_TIME,
			"-hls_playlist_type", HLS_PLAYLIST_TYPE,
			"-hls_segment_filename", hlsSegmentPath,
			"-f", HLS_FORMAT, hlsPlaylistPath
		));
	}

	private void addStreamMapCommands(List<String> commands, List<String> resolutions) {
		commands.addAll(Arrays.asList(
			VIDEO_STREAM_MAP_OPTIONS[0], VIDEO_STREAM_MAP_OPTIONS[1],
			"-var_stream_map", buildStreamMap(resolutions),
			"-master_pl_name", MASTER_M3U8_FILENAME
		));
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
