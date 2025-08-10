package com.vidcentral.api.infrastructure.storage;

import static com.vidcentral.global.common.util.VideoConstant.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vidcentral.global.error.exception.VIDCentralException;
import com.vidcentral.global.error.model.ErrorMessage;

@Service
public class FileStorageService {

	/**
	 * FileStorageService는 파일 시스템과의 상호작용을 전담하는 인프라 계층 클래스이다.
	 * 비디오 파일을 로컬 서버에 저장하거나 삭제하는 역할을 수행한다.
	 * 이 클래스는 파일 시스템의 세부 구현을 캡슐화하여, 다른 서비스들이 파일 저장소에 직접 접근하지 않도록 도와준다.
	 */

	public String saveFile(MultipartFile multipartFile) {
		try {
			final String originalFileName = multipartFile.getOriginalFilename();
			final String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
			final String uniqueFileName = UUID.randomUUID() + fileExtension;

			Path filePath = Paths.get(VIDEO_STORAGE_PATH, uniqueFileName);
			Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			return filePath.toString();
		} catch (IOException e) {
			throw new VIDCentralException(ErrorMessage.FILE_SAVE_ERROR);
		}
	}

	public void deleteFile(String filePath) {
		try {
			Files.deleteIfExists(Paths.get(filePath));
		} catch (IOException e) {
			throw new VIDCentralException(ErrorMessage.FILE_DELETE_ERROR);
		}
	}
}
