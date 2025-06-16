package com.vidcentral.global.common.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AESGcmUtil {

	private static final String TRANSFORMATION = "AES/GCM/NoPadding";
	private static final int IV_SIZE = 12;
	private static final int TAG_LENGTH_BIT = 128;

	private final SecretKey secretKey;
	private final SecureRandom secureRandom = new SecureRandom();

	public AESGcmUtil(@Value("${app.crypto.key}") String base64Key) {
		byte[] keyBytes = Base64.getDecoder().decode(base64Key);
		this.secretKey = new SecretKeySpec(keyBytes, "AES");
	}

	public String encrypt(String plainText) {
		try {
			byte[] iv = new byte[IV_SIZE];
			secureRandom.nextBytes(iv);

			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
			byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

			ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
			byteBuffer.put(iv).put(cipherText);
			return Base64.getEncoder().encodeToString(byteBuffer.array());
		} catch (Exception e) {
			log.error("[✅ LOGGER] AES-GCM 암호화 중 오류가 발생했습니다.");
			throw new IllegalStateException("[❎ ERROR] AES-GCM 암호화를 실패했습니다.");
		}
	}

	public String decrypt(String encryptedBase64) {
		try {
			byte[] decoded = Base64.getDecoder().decode(encryptedBase64);
			ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);

			byte[] iv = new byte[IV_SIZE];
			byteBuffer.get(iv);
			byte[] cipherText = new byte[byteBuffer.remaining()];
			byteBuffer.get(cipherText);

			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
			byte[] plain = cipher.doFinal(cipherText);
			return new String(plain, StandardCharsets.UTF_8);
		} catch (Exception e) {
			log.error("[✅ LOGGER] AES-GCM 복호화 중 오류가 발생했습니다.");
			throw new IllegalStateException("[❎ ERROR] AES-GCM 복호화를 실패했습니다.");
		}
	}
}
