package com.cow.fuelspot.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * QueryDSL 전역 설정 클래스
 * 모든 도메인(fuelQuiry 등)에서 JPAQueryFactory를 주입받아 사용할 수 있게 합니다.
 */
@Configuration
public class QuerydslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JPAQueryFactory를 빈으로 등록합니다.
     * 이제 RepositoryImpl에서 생성자 주입으로 바로 사용할 수 있습니다.
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
