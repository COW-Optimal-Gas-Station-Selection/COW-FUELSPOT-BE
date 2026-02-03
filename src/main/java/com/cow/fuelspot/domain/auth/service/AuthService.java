package com.cow.fuelspot.domain.auth.service;

import com.cow.fuelspot.domain.auth.dto.TokenDto;
import com.cow.fuelspot.domain.auth.entity.RefreshToken;
import com.cow.fuelspot.domain.auth.repository.RefreshTokenRepository;
import com.cow.fuelspot.domain.auth.dto.LoginRequest;
import com.cow.fuelspot.domain.auth.dto.LoginResponse;
import com.cow.fuelspot.domain.auth.dto.TokenReissueRequest;
import com.cow.fuelspot.domain.auth.dto.PasswordResetRequest;
import com.cow.fuelspot.domain.member.entity.Member;
import com.cow.fuelspot.domain.member.repository.MemberRepository;
import com.cow.fuelspot.global.jwt.JwtTokenProvider;
import com.cow.fuelspot.global.common.code.ErrorCode;
import com.cow.fuelspot.global.common.exception.CustomException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // 로그인
    @Transactional
    public LoginResponse login(LoginRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .email(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return LoginResponse.builder()
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
        if (!jwtTokenProvider.validateToken(request.getRefreshToken())) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(request.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGOUT_FAILED));

        if (!refreshToken.getValue().equals(request.getRefreshToken())) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);
        refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    // 로그아웃 (DB에서 Refresh Token 삭제)
    @Transactional
    public void logout(String email) {
        refreshTokenRepository.deleteById(email);
    }

    // 비밀번호 재설정
    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        emailService.verifyCode(request.getEmail(), request.getCode());

        if (!request.getNewPassword().equals(request.getCheckNewPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_CONFIRM_MISMATCH);
        }

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.changePassword(passwordEncoder.encode(request.getNewPassword()));

        emailService.deleteCode(request.getEmail());
    }

}