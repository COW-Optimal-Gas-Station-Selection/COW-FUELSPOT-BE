package com.cow.fuelspot.domain.auth.service;

import com.cow.fuelspot.domain.auth.dto.TokenDto;
import com.cow.fuelspot.domain.auth.entity.RefreshToken;
import com.cow.fuelspot.domain.auth.repository.RefreshTokenRepository;
import com.cow.fuelspot.domain.auth.dto.LoginRequest;
import com.cow.fuelspot.domain.auth.dto.LoginResponse;
import com.cow.fuelspot.domain.auth.dto.TokenReissueRequest; // 아까 만든 재발급 요청 DTO
import com.cow.fuelspot.domain.member.entity.Member;
import com.cow.fuelspot.domain.member.repository.MemberRepository;
import com.cow.fuelspot.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cow.fuelspot.domain.auth.dto.EmailRequest;
import com.cow.fuelspot.domain.auth.dto.EmailVerificationRequest;
import com.cow.fuelspot.domain.auth.dto.PasswordResetRequest;
import com.cow.fuelspot.domain.auth.entity.EmailVerification;
import com.cow.fuelspot.domain.auth.repository.EmailVerificationRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        // Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        // 실제 검증 (비밀번호 체크)
        // authenticate()가 실행될 때 CustomUserDetailsService의 loadUserByUsername이 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성 (Access + Refresh)
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        // RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .email(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        // 기존 토큰이 있으면 업데이트, 없으면 insert
        refreshTokenRepository.save(refreshToken);

        // 응답 객체 생성
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        return LoginResponse.builder()
                .isSuccess(true)
                .message("로그인 성공")
                .memberId(member.getId())
                .nickname(member.getNickname())
                .fuelType(member.getFuelType())
                .radius(member.getRadius())
                .tokenDto(tokenDto)
                .build();
    }

    // 토큰 재발급
    @Transactional
    public TokenDto reissue(TokenReissueRequest request) {
        // Refresh Token 유효성 검사
        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            throw new IllegalArgumentException("Refresh Token이 유효하지 않습니다.");
        }

        // Access Token 에서 회원 이메일 추출
        Authentication authentication = jwtTokenProvider.getAuthentication(request.getAccessToken());

        // DB에서 해당 회원 Refresh Token 가져오기
        RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("로그아웃 된 사용자입니다."));

        // 요청받은 토큰과 DB의 토큰 일치 여부 검사
        if (!refreshToken.getValue().equals(request.getRefreshToken())) {
            throw new IllegalArgumentException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 새로운 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        // 저장소 정보 업데이트 (RTR: Refresh Token도 교체)
        refreshToken.updateValue(tokenDto.getRefreshToken());

        return tokenDto;
    }

    // 로그아웃 (DB에서 Refresh Token 삭제)
    @Transactional
    public void logout(String email) {
        refreshTokenRepository.deleteById(email);
    }

    // 인증 코드 발송
    @Transactional
    public void sendVerificationCode(EmailRequest request) {
        // 가입된 이메일인지 확인
        if(!memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("가입되지 않은 이메일입니다.");
        }

        // 6자리 랜덤 숫자 코드 생성
        String code = createRandomCode();

        // Redis 저장
        EmailVerification verification = EmailVerification.builder()
                .email(request.getEmail())
                .code(code)
                .build();
        emailVerificationRepository.save(verification);

        // 이메일 전송 (비동기)
        emailService.sendEmail(request.getEmail(), "[FuelSpot] 비밀번호 찾기 인증 코드",
                "<h3>인증 코드: " + code + "</h3><p>5분 안에 입력해주세요.</p>");
    }

    // 인증 코드 검증
    public void verifyCode(EmailVerificationRequest request) {
        // Redis에서 이메일로 저장된 코드 조회
        EmailVerification verification = emailVerificationRepository.findById(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("인증 코드가 만료되었거나 잘못되었습니다."));

        // 저장된 코드와 입력받은 코드가 일치하는지 비교
        if (!verification.getCode().equals(request.getCode())) {
            throw new IllegalArgumentException("인증 코드가 일치하지 않습니다.");
        }
    }

    // 비밀번호 재설정
    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        // 인증 코드 재검증
        verifyCode(new EmailVerificationRequest(request.getEmail(), request.getCode()));

        // 회원 정보 조회
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 비밀번호 암호화 및 번경
        member.changePassword(passwordEncoder.encode(request.getNewPassword()));

        // 사용한 인증 코드 Redis에서 삭제
        emailVerificationRepository.deleteById(request.getEmail());
    }

    // 6자리 랜덤 숫자 생성
    private String createRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            key.append(random.nextInt(10));
        }
        return key.toString();
    }

}