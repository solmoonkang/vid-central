package com.vidcentral.api.infrastructure.s3;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vidcentral.api.application.port.StorageService;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3StorageService implements StorageService {

	private static final String S3_URL_PATTERN = "https://%s.s3.%s.amazonaws.com/%s";
	private static final String ACL_PUBLIC_READ = "public-read";

	private final S3Client s3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Override
	public String storeMediaFile(MultipartFile multipartFile, String mediaId) {
		return upload(multipartFile, "media/", mediaId, "file");
	}

	@Override
	public String storeThumbnailFile(MultipartFile multipartFile, String mediaId) {
		return upload(multipartFile, "media/", mediaId, "thumbnail");
	}

	private String upload(MultipartFile multipartFile, String directory, String groupId, String prefix) {
		final String key = generateS3Key(directory, groupId, prefix, multipartFile.getOriginalFilename());
		final PutObjectRequest putObjectRequest = buildPutObjectRequest(key, multipartFile);

		uploadToS3(putObjectRequest, multipartFile);

		return String.format(S3_URL_PATTERN, bucket, region, key);
	}

	private String generateS3Key(String directory, String groupId, String prefix, String originalFilename) {
		return String.format("%s%s/%s_%s", directory, groupId, prefix, originalFilename);
	}

	private PutObjectRequest buildPutObjectRequest(String key, MultipartFile multipartFile) {
		return PutObjectRequest.builder()
			.bucket(bucket)
			.key(key)
			.acl(ACL_PUBLIC_READ)
			.contentType(multipartFile.getContentType())
			.contentLength(multipartFile.getSize())
			.build();
	}

	private void uploadToS3(PutObjectRequest putObjectRequest, MultipartFile multipartFile) {
		try (InputStream inputStream = multipartFile.getInputStream()) {
			s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, multipartFile.getSize()));
		} catch (Exception e) {
			throw new IllegalStateException("[❎ ERROR] S3 업로드에 실패했습니다.");
		}
	}
}
