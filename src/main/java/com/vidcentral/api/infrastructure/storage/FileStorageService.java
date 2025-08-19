package com.vidcentral.api.infrastructure.storage;

import static com.vidcentral.global.common.util.VideoConstant.*;
import static java.nio.file.StandardOpenOption.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vidcentral.global.error.exception.InternalServerException;
import com.vidcentral.global.error.model.ErrorCode;

@Service
public class FileStorageService {

	public void createDirectories(String filePath) {
		try {
			Files.createDirectories(Paths.get(filePath));
		} catch (IOException e) {
			throw new InternalServerException(ErrorCode.DIRECTORY_CREATION_FAILED, filePath);
		}
	}

	public String createChunk(MultipartFile chunk, Long videoId, int chunkNumber) {
		final Path tempDirectoryPath = Paths.get(VIDEO_STORAGE_PATH, TEMP_DIRECTORY_NAME, videoId.toString());
		final Path chunkFilePath = tempDirectoryPath.resolve(chunkNumber + CHUNK_FILE_EXTENSION);

		try {
			Files.copy(chunk.getInputStream(), chunkFilePath, StandardCopyOption.REPLACE_EXISTING);
			return chunkFilePath.toString();
		} catch (IOException e) {
			throw new InternalServerException(ErrorCode.FILE_SAVE_FAILED);
		}
	}

	public String createMergedFile(List<String> chunkFilePaths, String originalFileName) {
		final String fileExtension = getFileExtension(originalFileName);
		final String uniqueFileName = UUID.randomUUID() + fileExtension;
		final Path finalFilePath = Paths.get(VIDEO_STORAGE_PATH, uniqueFileName);

		try {
			performFileMerge(finalFilePath, chunkFilePaths);
			return finalFilePath.toString();
		} catch (IOException e) {
			throw new InternalServerException(ErrorCode.FILE_MERGE_FAILED);
		}
	}

	public void deleteDirectory(String filePath) {
		try (Stream<Path> walk = Files.walk(Paths.get(filePath))) {
			walk.sorted(Comparator.reverseOrder())
				.forEach(this::deletePath);
		} catch (IOException e) {
			throw new InternalServerException(ErrorCode.DIRECTORY_DELETE_FAILED);
		}
	}

	public void deleteFile(String filePath) {
		try {
			Files.deleteIfExists(Paths.get(filePath));
		} catch (IOException e) {
			throw new InternalServerException(ErrorCode.FILE_DELETE_FAILED);
		}
	}

	private void performFileMerge(Path mergeedFilePath, List<String> chunkFilePaths) throws IOException {
		try (FileChannel outputChannel = FileChannel.open(mergeedFilePath, CREATE, WRITE)) {
			for (String chunkPath : chunkFilePaths) {
				try (FileChannel inputChannel = FileChannel.open(Paths.get(chunkPath), READ)) {
					outputChannel.transferFrom(inputChannel, outputChannel.size(), inputChannel.size());
				}
			}
		}
	}

	private void deletePath(Path pathToDelete) {
		try {
			Files.delete(pathToDelete);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf(FILE_EXTENSION_DELIMITER);
		if (dotIndex > 0)
			return fileName.substring(dotIndex);

		return DEFAULT_VIDEO_EXTENSION;
	}
}
