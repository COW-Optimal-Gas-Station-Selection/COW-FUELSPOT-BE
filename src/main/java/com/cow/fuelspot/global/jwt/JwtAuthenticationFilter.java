package com.cow.fuelspot.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

// JWT 인증 필터
// 클라이언트의 요청을 가장 먼저 가로채서,
// 헤더에 있는 JWT 토큰을 검사하고 로그인 처리(인증)를 수행
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider; //토큰 검사 도구

    // 필터 로직 (실제 검문이 이루어지는 메서드)
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 요청 헤더에서 토큰 추출 (HttpServletRequest: 웹 전용형)
        String token = resolveToken((HttpServletRequest) request);

        // 토큰 유효성 검사 (토큰이 존재하고, 위조/만료되지 않았는지)
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하다면, 인증 객체 생성
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // 생성된 인증 객체를 시큐리티 컨텍스트에 저장 (입장 허가)
            // SecurityContextHolder: 출입자 명부 (전역 변수)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 요청을 전달 (통과)
        chain.doFilter(request, response);
    }

    // 토큰 추출 메서드
    // 헤더에서 Authorization 값을 찾아 Bearer 접두사를 제거하고 토큰 반환
    private String resolveToken(HttpServletRequest request) {
        // 헤더에서 Authorization 부분 검색
        String bearerToken = request.getHeader("Authorization");

        // 값이 있고 Bearer로 시작하는지 확인
        // (JWT 토큰은 일반적으로 앞에 Bearer라는 글자를 붙여 전송)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // 앞의 Bearer 7글자를 자르고 뒤의 토큰만 리턴
        }
        return null; // 없거나 형식이 이상하면 null 반환
    }
}
