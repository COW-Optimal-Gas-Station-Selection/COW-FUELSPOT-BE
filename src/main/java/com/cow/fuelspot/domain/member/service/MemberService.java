package com.cow.fuelspot.domain.member.service;

import com.cow.fuelspot.domain.member.dto.MemberSignupRequest;
import com.cow.fuelspot.domain.member.entity.Member;
import com.cow.fuelspot.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cow.fuelspot.domain.member.dto.LoginRequest;
import com.cow.fuelspot.domain.member.dto.LoginResponse;
import com.cow.fuelspot.global.jwt.JwtTokenProvider;

// 회원 서비스 계층
// 회원의 비즈니스 로직 처리 (회원가입, 로그인)
@Service
@RequiredArgsConstructor // 의존성 주입
@Transactional(readOnly = true)
public class MemberService {

    // 생성자 주입
    private final MemberRepository memberRepository; // DB 접근 도구
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화 도구
    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성 도구

    // 회원가입 기능
    // 이메일 중복 체크, 비밀번호 암호화, DB 저장
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

    // 로그인 기능
    // 이메일 존재 여부 확인, 비밀번호 일치 여부 확인, 인증 성공 시 JWT 토큰 발급 및 사용자 정보 반환
    public LoginResponse login(LoginRequest request) {
        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 비밀번호 검증
        // 사용자가 직접 입력한 비번(requset)과 DB에 있는 암호화된 비번(member)을 비교
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // (인증 성공 후) 토큰 생성
        String accessToken = jwtTokenProvider.createToken(member.getEmail());

        // 응답 객체 생성
        // 프론트엔드에 필요한 정보들을 모두 담아서 반환
        return LoginResponse.builder()
                .isSuccess(true)
                .message("로그인 성공")
                .memberId(member.getId()) // 회원 고유 ID
                .accessToken(accessToken) // 인증 토큰
                .nickname(member.getNickname()) // 닉네임
                .fuelType(member.getFuelType()) // 선호 유종
                .radius(member.getRadius()) // 선호 반경
                .build();
    }

}
