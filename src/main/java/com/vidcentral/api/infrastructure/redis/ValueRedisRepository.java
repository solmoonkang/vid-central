package com.vidcentral.api.infrastructure.redis;

import static java.util.Objects.*;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ValueRedisRepository {

	private final StringRedisTemplate stringRedisTemplate;

	public void set(String key, String value) {
		stringRedisTemplate.opsForValue().set(requireNonNull(key), requireNonNull(value));
	}

	public String get(String key) {
		return stringRedisTemplate.opsForValue().get(requireNonNull(key));
	}

	public void delete(String key) {
		stringRedisTemplate.delete(requireNonNull(key));
	}
}
