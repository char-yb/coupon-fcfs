package com.fcfs.api.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

// SET을 관리할 repository
@Repository
@RequiredArgsConstructor
public class AppliedUserRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public Long add(Long userId) {
		return redisTemplate
			.opsForSet()
			.add("applied_user", userId.toString());
	}
}
