package com.vidcentral.api.domain.image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NewImage implements MultipartFile {

	private final String name;
	private final String contentType;
	private final long size;
	private final byte[] bytes;

	public static NewImage of(String name, String contentType, byte[] bytes) {
		return new NewImage(name, contentType, bytes.length, bytes);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getOriginalFilename() {
		return name;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public long getSize() {
		return size;
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream(bytes);
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		try (FileOutputStream fileOutputStream = new FileOutputStream(dest)) {
			fileOutputStream.write(this.getBytes());
		}
	}
}
