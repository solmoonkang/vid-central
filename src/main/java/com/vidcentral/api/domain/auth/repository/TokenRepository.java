package com.vidcentral.api.domain.auth.repository;

import static com.vidcentral.global.common.util.RedisConstant.*;

import java.time.Duration;

import org.springframework.stereotype.Repository;

import com.vidcentral.api.dto.auth.response.TokenSaveResponse;
import com.vidcentral.api.infrastructure.redis.HashRedisRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TokenRepository {

	private final HashRedisRepository hashRedisRepository;

	public void saveToken(String email, TokenSaveResponse tokenSaveResponse) {
		hashRedisRepository
			.save(REDIS_REFRESH_TOKEN_PREFIX + email, tokenSaveResponse, Duration.ofMinutes(TOKEN_EXPIRE_MINUTES));
	}

	public TokenSaveResponse getTokenSaveValue(String email) {
		return (TokenSaveResponse)hashRedisRepository.get(REDIS_REFRESH_TOKEN_PREFIX + email);
	}
}
