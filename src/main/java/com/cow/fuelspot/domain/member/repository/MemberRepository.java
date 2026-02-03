package com.cow.fuelspot.domain.member.repository;

import com.cow.fuelspot.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 회원 레포지토리
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    // NullPointerException 에러를 방지하기 위해 Optional 사용
    Optional<Member> findByEmail(String email);
}
