package com.vidcentral.api.application.video.upload;

import static com.vidcentral.global.common.util.VideoConstant.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vidcentral.global.error.exception.BadRequestException;
import com.vidcentral.global.error.exception.VIDCentralException;
import com.vidcentral.global.error.model.ErrorMessage;

@Service
public class FFmpegService {

	/**
	 * FFmpegService는 비디오 파일 자체를 가공하는 역할을 담당한다.
	 * 이 클래스는 외부 프로그램인 FFmpeg을 활용하여 영상 인코딩, 압축, 그리고 썸네일 생성과 같은 시간이 오래 걸리는 작업을 수행한다.
	 */

	public List<String> processVideo(String inputFilePath, List<String> resolutions, String baseFileName) {
		return resolutions.stream()
			.map(resolution -> encodeVideo(inputFilePath, resolution, baseFileName))
			.toList();
	}

	public String createThumbnail(String inputFilePath, String baseFileName) {
		final String thumbnailFilePath = THUMBNAIL_PATH + baseFileName + THUMBNAIL_EXTENSION;

		List<String> command = new ArrayList<>(Arrays.asList(FFMPEG_EXECUTABLE_PATH, "-i", inputFilePath));
		command.addAll(Arrays.asList(THUMBNAIL_OPTIONS));
		command.add(thumbnailFilePath);

		ProcessBuilder processBuilder = new ProcessBuilder(command);
		executeCommand(processBuilder, "썸네일 생성에 실패했습니다.");
		return thumbnailFilePath;
	}

	private String encodeVideo(String inputFilePath, String resolution, String baseFileName) {
		final String outputFileName = getOutputFileName(baseFileName, resolution);
		final String outputFilePath = PROCESSED_VIDEO_PATH + outputFileName;

		List<String> command = new ArrayList<>(Arrays.asList(FFMPEG_EXECUTABLE_PATH, "-i", inputFilePath));
		command.addAll(Arrays.asList(VIDEO_ENCODING_OPTIONS));
		command.add(outputFilePath);

		ProcessBuilder processBuilder = new ProcessBuilder(command);
		executeCommand(processBuilder, "FFmpeg 인코딩에 실패했습니다.");
		return outputFilePath;
	}

	private String getOutputFileName(String baseFileName, String resolution) {
		return switch (resolution) {
			case RESOLUTION_480P_VALUE -> baseFileName + RESOLUTION_480P_SUFFIX;
			case RESOLUTION_720P_VALUE -> baseFileName + RESOLUTION_720P_SUFFIX;
			case RESOLUTION_1080P_VALUE -> baseFileName + RESOLUTION_1080P_SUFFIX;
			default -> throw new BadRequestException(ErrorMessage.INVALID_RESOLUTION);
		};
	}

	private void executeCommand(ProcessBuilder processBuilder, String errorMessage) {
		try {
			Process process = processBuilder.start();
			int exitCode = process.waitFor();

			if (exitCode != 0) {
				String errorOutput = new String(process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
				throw new RuntimeException(errorMessage + ": " + errorOutput);
			}
		} catch (IOException | InterruptedException e) {
			throw new VIDCentralException(ErrorMessage.VIDEO_PROCESSING_ERROR);
		}
	}
}
