package com.cow.fuelspot.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Key;
import java.util.Date;

// JWT 토큰 관리자
// 토큰을 생성, 검증, 정보 추출, 인증 객체 조회를 담당
@Component
public class JwtTokenProvider {

    private final Key key; // 토큰을 암호화(서명)할 때 사용할 비밀키
    private final long accessTokenValidityInMilliseconds; // 액세스 토큰의 유효 시간 (1시간)
    private final UserDetailsService userDetailsService; // 회원 정보를 DB에서 찾아오는 역할

    // 생성자: application.yml에서 비밀키를 가져와서 초기화
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            UserDetailsService userDetailsService) {
        // Base64로 인코딩된 비밀키를 디코딩하여 바이트 배열로 변환
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // 바이트 배열을 기반으로 암호화 키 객체 생성
        this.key = Keys.hmacShaKeyFor(keyBytes);
        // 토큰 유효 시간 설정: 1시간
        this.accessTokenValidityInMilliseconds = 1000 * 60 * 60;
        this.userDetailsService = userDetailsService;
    }

    // 토큰 발급
    // 로그인 성공 시 유저 이메일 정보를 받아 JWT 토큰 생성
    public String createToken(String email) {
        long now = (new Date()).getTime();
        // 만료 시간 계산
        Date validity = new Date(now + this.accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(email) // 토큰의 주인으로 이메일 설정
                .setIssuedAt(new Date()) // 토큰 발행 시간 설정
                .setExpiration(validity) // 토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // 비밀키와 HS256 알고리즘으로 서명(암호화)
                .compact(); // 설정된 내용들을 압축하여 문자열로 반환
    }

    // 인증 객체 생성
    // 토큰 -> DB 회원 조회 -> 로그인 상태 확정
    public Authentication getAuthentication(String token) {
        // 토큰에서 이메일 추출
        String email = getEmail(token);
        // DB에서 디테일한 회원 정보 가져오기 (존재 여부 확인)
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        // 스프링 시큐리티가 인증되었다고 인정하는 공식 신분증 반환
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 이메일 추출
    // 토큰에 들어있는 이메일을 꺼냄
    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // 비밀키를 설정하여 서명 확인
                .build()
                .parseClaimsJws(token) // 토큰 해석
                .getBody() // 내용물을 가져옴
                .getSubject(); // 저장된 이메일 반환
    }

    // 토큰 검증
    // 토큰이 유효한지(위조, 만료) 검사
    public boolean validateToken(String token) {
        try {
            // 토큰 해석 시도. 만약 문제가 있다면 예외 발생
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("잘못된 JWT 서명입니다."); // 서명이 일치하지 않음 (위조 가능성)
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT 토큰입니다."); // 유효시간 만료
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT 토큰입니다."); // 형식이 다름
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 잘못되었습니다."); // 토큰 값이 비어있음
        }
        return false;
    }
}
