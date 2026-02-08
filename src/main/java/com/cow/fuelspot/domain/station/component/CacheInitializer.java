package com.cow.fuelspot.domain.station.component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheInitializer {

    private final GasStationCacheManager cacheManager;

    /**
     * 서버 시작 시 캐시 초기화
     * 배포 환경에서 이전 캐시 데이터로 인한 문제 방지
     */
    @PostConstruct
    public void initializeCache() {
        log.info("서버 시작 - 캐시 초기화 시작");
        cacheManager.clearAllCache();
        log.info("캐시 초기화 완료");
    }
}