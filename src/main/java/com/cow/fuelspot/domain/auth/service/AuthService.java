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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        // Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        // 실제로 검증이 일어나는 부분 (비밀번호 체크)
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

    @Transactional
    public TokenDto reissue(TokenReissueRequest request) {
        // Refresh Token 검증
        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            throw new IllegalArgumentException("Refresh Token이 유효하지 않습니다.");
        }

        // Access Token 에서 Member ID (email) 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(request.getAccessToken());

        // 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("로그아웃 된 사용자입니다."));

        // Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(request.getRefreshToken())) {
            throw new IllegalArgumentException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 새로운 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        // 저장소 정보 업데이트 (RTR: Refresh Token도 교체)
        refreshToken.updateValue(tokenDto.getRefreshToken());

        return tokenDto;
    }

    @Transactional
    public void logout(String email) {
        refreshTokenRepository.deleteById(email);
    }
}