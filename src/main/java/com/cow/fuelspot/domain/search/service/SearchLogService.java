package com.cow.fuelspot.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchLogService {

    private final StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "searchlog:log:";

    private static final int MAX_LOG_COUNT = 5;

    // 검색어 저장
    public void saveSearchKeyword(String email, String keyword) {
        String key = KEY_PREFIX + email;
        ZSetOperations<String, String> zSet = redisTemplate.opsForZSet();

        zSet.add(key, keyword, System.currentTimeMillis());

        zSet.removeRange(key, 0, -(MAX_LOG_COUNT + 1));
    }

    // 최근 검색어 목록 조회
    public Set<String> getRecentKeywords(String email) {
        String key = KEY_PREFIX + email;
        ZSetOperations<String, String> zSet = redisTemplate.opsForZSet();

        return zSet.reverseRange(key, 0, -1);
    }

    // 특정 검색어 삭제
    public void deleteKeyword(String email, String keyword) {
        String key = KEY_PREFIX + email;
        redisTemplate.opsForZSet().remove(key, keyword);
    }

    // 전체 삭제
    public void deleteAllKeywords(String email) {
        String key = KEY_PREFIX + email;
        redisTemplate.delete(key);
    }
}
