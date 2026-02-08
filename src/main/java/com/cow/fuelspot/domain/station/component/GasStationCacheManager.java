package com.cow.fuelspot.domain.station.component;

import com.cow.fuelspot.domain.station.dto.opinet.OpinetAverageDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetDetailDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetNearbyDto;
import com.cow.fuelspot.domain.station.dto.opinet.OpinetSidoAverageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class GasStationCacheManager {

    // 한국 시간대
    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    // 캐시 갱신 시간 (1시, 2시, 9시, 12시, 16시, 19시)
    private static final int[] CACHE_UPDATE_HOURS = {1, 2, 9, 12, 16, 19};

    // 오피넷 배치 처리 시간 고려 (10분 소요 + 5분 여유 = 15분)
    private static final int BATCH_DELAY_MINUTES = 15;

    // 근처 주유소 캐시 (key: lat_lon_radius_fuelType, value: CacheData)
    private final Map<String, CacheData<List<OpinetNearbyDto>>> nearbyCache = new ConcurrentHashMap<>();

    // 상세 정보 캐시 (key: stationId, value: CacheData)
    private final Map<String, CacheData<OpinetDetailDto>> detailCache = new ConcurrentHashMap<>();

    // 전국 평균 캐시
    private CacheData<List<OpinetAverageDto>> averageCache;

    // 시도별 평균 캐시 (key: sidoCode, value: CacheData)
    private final Map<String, CacheData<List<OpinetSidoAverageDto>>> sidoAverageCache = new ConcurrentHashMap<>();

    /**
     * 근처 주유소 캐시 조회
     */
    public List<OpinetNearbyDto> getNearbyCache(String key) {
        CacheData<List<OpinetNearbyDto>> cache = nearbyCache.get(key);
        if (cache != null && isCacheValid(cache.getCachedTime())) {
            log.info(" 근처 주유소 캐시 사용: {}", key);
            return cache.getData();
        }
        log.info(" 근처 주유소 캐시 만료 또는 없음: {}", key);
        return null;
    }

    /**
     * 근처 주유소 캐시 저장
     */
    public void putNearbyCache(String key, List<OpinetNearbyDto> data) {
        nearbyCache.put(key, new CacheData<>(data, getCurrentKoreaTime()));
        log.info(" 근처 주유소 캐시 저장: {}", key);
    }

    /**
     * 상세 정보 캐시 조회
     */
    public OpinetDetailDto getDetailCache(String stationId) {
        CacheData<OpinetDetailDto> cache = detailCache.get(stationId);
        if (cache != null && isCacheValid(cache.getCachedTime())) {
            log.info(" 상세 정보 캐시 사용: {}", stationId);
            return cache.getData();
        }
        log.info(" 상세 정보 캐시 만료 또는 없음: {}", stationId);
        return null;
    }

    /**
     * 상세 정보 캐시 저장
     */
    public void putDetailCache(String stationId, OpinetDetailDto data) {
        detailCache.put(stationId, new CacheData<>(data, getCurrentKoreaTime()));
        log.info(" 상세 정보 캐시 저장: {}", stationId);
    }

    /**
     * 전국 평균 캐시 조회
     */
    public List<OpinetAverageDto> getAverageCache() {
        if (averageCache != null && isAverageCacheValid(averageCache.getCachedTime())) {
            log.info("전국 평균 캐시 사용");
            return averageCache.getData();
        }
        log.info("전국 평균 캐시 만료 또는 없음");
        return null;
    }

    /**
     * 전국 평균 캐시 저장
     */
    public void putAverageCache(List<OpinetAverageDto> data) {
        averageCache = new CacheData<>(data, getCurrentKoreaTime());
        log.info(" 전국 평균 캐시 저장");
    }

    /**
     * 시도별 평균 캐시 조회
     */
    public List<OpinetSidoAverageDto> getSidoAverageCache(String sidoCode) {
        CacheData<List<OpinetSidoAverageDto>> cache = sidoAverageCache.get(sidoCode);
        if (cache != null && isAverageCacheValid(cache.getCachedTime())) {
            log.info("시도별 평균 캐시 사용: {}", sidoCode);
            return cache.getData();
        }
        log.info("시도별 평균 캐시 만료 또는 없음: {}", sidoCode);
        return null;
    }

    /**
     * 시도별 평균 캐시 저장
     */
    public void putSidoAverageCache(String sidoCode, List<OpinetSidoAverageDto> data) {
        sidoAverageCache.put(sidoCode, new CacheData<>(data, getCurrentKoreaTime()));
        log.info(" 시도별 평균 캐시 저장: {}", sidoCode);
    }

    /**
     * 캐시 유효성 검사
     * 마지막 갱신 시간(1시, 2시, 9시, 12시, 16시, 19시) 이후인지 확인
     */
    private boolean isCacheValid(LocalDateTime cachedTime) {
        LocalDateTime lastUpdateTime = getLastUpdateTime();
        return cachedTime.isAfter(lastUpdateTime) || cachedTime.isEqual(lastUpdateTime);
    }

    /**
     * 평균 캐시 유효성 검사
     * 하루(24시간) 기준 - 당일 자정 이후 캐시 유효
     */
    private boolean isAverageCacheValid(LocalDateTime cachedTime) {
        LocalDateTime lastUpdateTime = getAverageLastUpdateTime();
        return cachedTime.isAfter(lastUpdateTime) || cachedTime.isEqual(lastUpdateTime);
    }

    /**
     * 마지막 갱신 시간 계산
     * 현재 시간 기준으로 가장 최근의 갱신 시간을 반환
     * 오피넷 배치 처리 시간(약 10분)을 고려하여 15분 여유를 둠
     */
    private LocalDateTime getLastUpdateTime() {
        LocalDateTime now = getCurrentKoreaTime();
        int currentHour = now.getHour();
        int currentMinute = now.getMinute();

        // 현재 시간보다 이전인 가장 가까운 갱신 시간 찾기
        int lastUpdateHour = CACHE_UPDATE_HOURS[0]; // 기본값: 1시
        for (int hour : CACHE_UPDATE_HOURS) {
            if (hour < currentHour) {
                lastUpdateHour = hour;
            } else if (hour == currentHour && currentMinute >= BATCH_DELAY_MINUTES) {
                // 같은 시간대지만 배치 완료 시간(15분) 이후면 갱신됨
                lastUpdateHour = hour;
            } else {
                break;
            }
        }

        // 현재 시간이 1시 15분 이전이면 전날 19시 15분을 기준으로 설정
        if (currentHour < CACHE_UPDATE_HOURS[0] ||
                (currentHour == CACHE_UPDATE_HOURS[0] && currentMinute < BATCH_DELAY_MINUTES)) {
            return now.minusDays(1)
                    .with(LocalTime.of(CACHE_UPDATE_HOURS[CACHE_UPDATE_HOURS.length - 1], BATCH_DELAY_MINUTES, 0));
        }

        return now.with(LocalTime.of(lastUpdateHour, BATCH_DELAY_MINUTES, 0));
    }

    /**
     * 평균 데이터 마지막 갱신 시간 계산
     * 당일 자정(00:00) 기준
     */
    private LocalDateTime getAverageLastUpdateTime() {
        LocalDateTime now = getCurrentKoreaTime();
        return now.with(LocalTime.of(0, 0, 0));
    }

    /**
     * 현재 한국 시간 반환
     */
    private LocalDateTime getCurrentKoreaTime() {
        return ZonedDateTime.now(KOREA_ZONE).toLocalDateTime();
    }

    /**
     * 캐시 데이터 wrapper 클래스
     */
    private static class CacheData<T> {
        private final T data;
        private final LocalDateTime cachedTime;

        public CacheData(T data, LocalDateTime cachedTime) {
            this.data = data;
            this.cachedTime = cachedTime;
        }

        public T getData() {
            return data;
        }

        public LocalDateTime getCachedTime() {
            return cachedTime;
        }
    }

    /**
     * 근처 주유소 캐시 키 생성
     */
    public String generateNearbyKey(double lat, double lon, int radius, String fuelTypeCode) {
        return String.format("%.6f_%.6f_%d_%s", lat, lon, radius, fuelTypeCode);
    }

    /**
     * 전체 캐시 초기화 (테스트 또는 관리 목적)
     */
    public void clearAllCache() {
        nearbyCache.clear();
        detailCache.clear();
        averageCache = null;
        sidoAverageCache.clear();
        log.warn("🗑️ 전체 캐시 초기화 완료");
    }
}