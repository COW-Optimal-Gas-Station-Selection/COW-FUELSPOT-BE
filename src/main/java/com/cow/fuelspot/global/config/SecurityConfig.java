package com.cow.fuelspot.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 보안 설정 클래스
@Configuration
@EnableWebSecurity // 보안 기능 활성화
public class SecurityConfig {

    // 보안 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보안 비활성화 (CSRF : 쿠키/세션 기반 인증에서 해커가 사용자의 로그인을 도용하는 공격)
                // JWT(토큰) 방식 사용 예정 & Postman 테스트 편의를 위해 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // 출입 권한 설정 (어떤 주소로 들어올 때 검사를 할지 말지 정하는 곳)
                .authorizeHttpRequests(auth -> auth
                        // 스웨거 관련 주소 2개를 "프리패스"로 설정!
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // "/api/members" 로 오는 요청(회원가입)은 무조건 허용(permitAll)
                        .requestMatchers("/api/members").permitAll()
                        .requestMatchers("/api/gas-stations/**").permitAll()
                        // 그 외의 모든 요청은 인증된(로그인한) 사람만 통과
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    // 비밀번호 암호화 도구 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 강력하고 대중적인 해시 암호화 도구
    }
}
