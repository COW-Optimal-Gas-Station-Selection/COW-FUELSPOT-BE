package com.cow.fuelspot.domain.member.repository;

import com.cow.fuelspot.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 회원 레포지토리
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일 중복 검사 (ture, false 반환)
    boolean existsByEmail(String email);

    // 이메일로 회원 정보 조회 (로그인, 내 정보 조회용)
    // NullPointerException 에러를 방지하기 위해 Optional 사용
    Optional<Member> findByEmail(String email);
}
