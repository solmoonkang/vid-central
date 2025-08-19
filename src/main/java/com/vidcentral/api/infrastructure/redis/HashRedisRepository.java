package com.vidcentral.api.infrastructure.redis;

import static java.util.Objects.*;

import java.util.Map;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HashRedisRepository {

	private final StringRedisTemplate stringRedisTemplate;

	public void put(String key, String hashKey, String value) {
		stringRedisTemplate.opsForHash().put(requireNonNull(key), requireNonNull(hashKey), requireNonNull(value));
	}

	public Map<Object, Object> getEntries(String key) {
		return stringRedisTemplate.opsForHash().entries(requireNonNull(key));
	}

	public Long getSize(String key) {
		return stringRedisTemplate.opsForHash().size(requireNonNull(key));
	}

	public Boolean hasKey(String key, String hashKey) {
		return stringRedisTemplate.opsForHash().hasKey(requireNonNull(key), requireNonNull(hashKey));
	}

	public void delete(String key, Object... hashKeys) {
		stringRedisTemplate.opsForHash().delete(requireNonNull(key), hashKeys);
	}
}
