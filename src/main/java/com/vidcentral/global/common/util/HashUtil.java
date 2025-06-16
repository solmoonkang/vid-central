package com.vidcentral.global.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashUtil {

	public static String SHA256Hex(String input) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] digest = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));

			StringBuilder hashBuilder = new StringBuilder();
			for (byte b : digest) {
				hashBuilder.append(String.format("%02x", b));
			}

			return hashBuilder.toString();
		} catch (Exception e) {
			throw new IllegalStateException("[❎ ERROR] SHA-256 해시 생성에 실패했습니다.");
		}
	}
}
