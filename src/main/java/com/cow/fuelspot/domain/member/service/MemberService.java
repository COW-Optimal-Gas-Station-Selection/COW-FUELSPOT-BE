package com.cow.fuelspot.domain.member.service;

import com.cow.fuelspot.domain.member.dto.MemberSignupRequest;
import com.cow.fuelspot.domain.member.entity.Member;
import com.cow.fuelspot.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 회원 비즈니스 로직 담당 (회원가입, 정보 수정)
@Service
@RequiredArgsConstructor // 의존성 주입
@Transactional(readOnly = true)
public class MemberService {

    // 생성자 주입
    private final MemberRepository memberRepository; // DB 작업용
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화용

    // 회원가입 기능 (이메일 중복 체크, 비밀번호 암호화, DB 저장)
    @Transactional
    public void signup(MemberSignupRequest request) {
        // 이메일 중복 검사
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // DTO -> Entity 변환 (DTO 속 toEntity 메서드 사용)
        Member member = request.toEntity(encodedPassword);

        // DB 저장
        memberRepository.save(member);
    }
}
