package com.vidcentral.api.infrastructure.storage;

import static com.vidcentral.global.common.util.VideoConstant.*;
import static java.nio.file.StandardOpenOption.*;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vidcentral.global.error.exception.VIDCentralException;
import com.vidcentral.global.error.model.ErrorMessage;

@Service
public class FileStorageService {

	public String saveChunk(MultipartFile chunk, Long videoId, int chunkNumber) {
		try {
			final Path tempDirectoryPath = Paths.get(VIDEO_STORAGE_PATH, TEMP_DIRECTORY_NAME, videoId.toString());
			Files.createDirectories(tempDirectoryPath);

			final Path chunkFilePath = tempDirectoryPath.resolve(chunkNumber + CHUNK_FILE_EXTENSION);
			Files.copy(chunk.getInputStream(), chunkFilePath, StandardCopyOption.REPLACE_EXISTING);
			return chunkFilePath.toString();
		} catch (IOException e) {
			throw new VIDCentralException(ErrorMessage.FILE_SAVE_ERROR);
		}
	}

	public String mergeChunks(Long videoId, List<String> chunkFilePaths, String originalFileName) {
		try {
			final String fileExtension = getFileExtension(originalFileName);
			final String uniqueFileName = UUID.randomUUID() + fileExtension;
			final Path finalFilePath = Paths.get(VIDEO_STORAGE_PATH, uniqueFileName);

			performFileMerge(finalFilePath, chunkFilePaths);

			for (String chunkPath : chunkFilePaths) {
				deleteFile(chunkPath);
			}
			deleteFile(Paths.get(VIDEO_STORAGE_PATH, TEMP_DIRECTORY_NAME, videoId.toString()).toString());

			return finalFilePath.toString();
		} catch (IOException e) {
			throw new VIDCentralException(ErrorMessage.FILE_MERGE_ERROR);
		}
	}

	public void deleteFile(String filePath) {
		try {
			Files.deleteIfExists(Paths.get(filePath));
		} catch (IOException e) {
			throw new VIDCentralException(ErrorMessage.FILE_DELETE_ERROR);
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

	private String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf(FILE_EXTENSION_DELIMITER);
		if (dotIndex > 0)
			return fileName.substring(dotIndex);

		return DEFAULT_VIDEO_EXTENSION;
	}
}
