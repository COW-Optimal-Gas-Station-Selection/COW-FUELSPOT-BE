package com.cow.fuelspot.global.jwt;

import com.cow.fuelspot.domain.auth.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

// JWT 토큰 관리자
// 토큰을 생성, 검증, 정보 추출, 인증 객체 조회를 담당
@Slf4j
@Component
public class JwtTokenProvider {

    // 토큰을 만들 때 사용하는 재료
    private static final String AUTHORITIES_KEY = "auth"; // 권한 정보의 키값
    private static final String BEARER_TYPE = "Bearer"; // 토큰 타입
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30 ; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    private final Key key; // 토큰 암호화용 비밀키

    // 생성자 (yml 파일에 있는 비밀번호를 가져와 Key 객체 생성)
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // 디코딩
        this.key = Keys.hmacShaKeyFor(keyBytes); // 키 객체 변환
    }

    // 토큰 생성
    // 인증 객체(Authentication) -> Access + Refresh 토큰 생성
    public TokenDto generateTokenDto(Authentication authentication) {
        // 권한 불러오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime(); // 현재 시간

        // Access Token 생성 (내용물: 유저 이름, 권한, 만료시간)
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName()) // 이메일
                .claim(AUTHORITIES_KEY, authorities) // 권한 정보
                .setExpiration(accessTokenExpiresIn) // 유효기간
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성 (내용물: 만료시간)
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    // 인증 객체 생성
    // 토큰 -> DB 회원 조회 -> 로그인 상태 확정
    public Authentication getAuthentication(String accessToken) {
        // 토큰 분해
        Claims claims = parseClaims(accessToken);

        // 권한 정보 확인
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 권한 목록 생성
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 유저 객체 생성
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 검증
    // 토큰이 유효한지(위조, 만료) 검사
    public boolean validateToken(String token) {
        try {
            // 토큰 해석 시도. 만약 문제가 있다면 예외 발생
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다."); // 서명이 일치하지 않음 (위조 가능성)
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다."); // 유효시간 만료
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다."); // 형식이 다름
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다."); // 토큰 값이 비어있음
        }
        return false;
    }

    // Claims: 토큰 안에 저장된 정보 조각들
    private Claims parseClaims(String accessToken) {
        try {
            //
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
